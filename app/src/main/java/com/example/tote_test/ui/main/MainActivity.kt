package com.example.tote_test.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.tote_test.R
import com.example.tote_test.databinding.ActivityMainBinding
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.models.GameModel
import com.example.tote_test.models.PrognosisModel
import com.example.tote_test.models.StakeModel
import com.example.tote_test.ui.tabs.TabsFragment
import com.example.tote_test.utils.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    private val viewModel: MainViewModel by viewModels()
    private lateinit var stakes: List<StakeModel>
    //private var stakes = arrayListOf<List<StakeModel>>()

    private var topLevelDestinations = setOf(
        getMainDestination(),
        getTabsDestination(),
        getProfileDestination()
    )

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is TabsFragment || f is NavHostFragment) return

            onNavControllerActivated(f.findNavController())
        }
    }

    private val destinationListener = NavController.OnDestinationChangedListener { _, destination, _ ->
        supportActionBar?.title = destination.label
        supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        APP_ACTIVITY = this

        observeStakesAll()
        observeGambler()
        observeGames()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        if (GAMBLER.photoUrl != EMPTY) loadAppBarPhoto()

        val navController = (supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment).navController
        prepareRootNavController(navController)
        onNavControllerActivated(navController)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)

        binding.gamblerPhoto.setOnClickListener {
            navController.navigate(R.id.profileFragment)
        }

        setCopyright()

        /*navController = (supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment).navController
        val graph = navController.navInflater.inflate(R.navigation.main_graph)
        graph.setStartDestination(R.id.startFragment)
        navController.graph = graph*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.menu_item_admin).isVisible = GAMBLER.admin

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_exit -> {
                viewModel.signOut()
                finish()
            }
            R.id.menu_item_admin -> {
                ((supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment).navController)
                    .navigate(R.id.admin_graph)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeGambler() = viewModel.gambler.observe(this) {
        GAMBLER = it
    }

    private fun observeStakesAll() = viewModel.stakesAll.observe(this) {
        stakes = it
    }

    @SuppressLint("SimpleDateFormat")
    private fun observeGames() = viewModel.games.observe(this) {
        stakes.map { stake ->
            val gambler = viewModel.gamblers.value?.find { gambler -> gambler.nickname == stake.gamblerId }
            if (gambler != null) {
                stake.gamblerId = gambler.id
            }
        }

        val now = Calendar.getInstance().time.time
        val nowLocale = SimpleDateFormat("dd.MM.yyyy HH:mm").parse(now.toString().asTime(toLocale = true))?.time ?: 0

        val games =
            it.filter { item -> (nowLocale > item.start.toLong()) && item.goal1.isNotBlank() && item.goal2.isNotBlank() }
                .sortedBy { item -> item.id }

        if (games.isNotEmpty()) {
            val gamblersCount = (viewModel.gamblers.value?.size ?: 0).toDouble()

            val gamesId = arrayListOf<Int>()

            games.forEach { game ->
                val stakesForGame = stakes.filter { item -> item.gameId == game.id }

                calcPrognosis(game, stakesForGame, gamblersCount)

                calcStakePoints(stakesForGame)

                gamesId.add(game.id)
            }

            calcGamblerPlaceAndPoints(gamesId)
        }
    }

    private fun calcPrognosis(game: GameModel, stakesForGame: List<StakeModel>, gamblersCount: Double) {
        val stakesWinCount = stakesForGame.filter { stake -> stake.goal1.isNotBlank() && stake.goal1 > stake.goal2 }.size
        val stakesDrawCount = stakesForGame.filter { stake -> stake.goal1.isNotBlank() && stake.goal1 == stake.goal2 }.size
        val stakesDefeatCount = stakesForGame.filter { stake -> stake.goal1.isNotBlank() && stake.goal1 < stake.goal2 }.size

        val coefficientForWin = if (stakesWinCount > 0) gamblersCount / stakesWinCount else 0.0
        val coefficientForDraw = if (stakesDrawCount > 0) gamblersCount / stakesDrawCount else 0.0
        val coefficientForDefeat = if (stakesDefeatCount > 0) gamblersCount / stakesDefeatCount else 0.0
        val coefficientForFine = -((coefficientForWin + coefficientForDraw + coefficientForDefeat) / 3)

        stakesForGame.forEach { stake ->
            val coefficient = if (stake.goal1 > stake.goal2) {
                coefficientForWin
            } else if (stake.goal1 == stake.goal2) {
                coefficientForDraw
            } else {
                coefficientForDefeat
            }

            stake.points = if (game.goal1.isBlank() || game.goal2.isBlank()) {
                0.0
            } else if (stake.goal1.isBlank()) {
                coefficientForFine
            } else {
                calcPoints(stake, game, coefficient, gamblersCount)
            }

            if (game.addGoal1.isNotBlank() && game.addGoal2.isNotBlank()
                && stake.addGoal1.isNotBlank() && stake.addGoal2.isNotBlank()
            ) {
                calcPointsForAddTime(stake, game)
            }
        }

        val gameResult = "${game.team1} - ${game.team2}" +
                if (game.goal1.isNotBlank() && game.goal2.isNotBlank()) {
                    " ${game.goal1}:${game.goal2}" +
                            if (game.addGoal1.isNotBlank() && game.addGoal2.isNotBlank()) {
                                ", доп.время ${game.addGoal1}:${game.addGoal2}" +
                                        if (game.penalty.isNotBlank()) {
                                            ", по пенальти ${game.penalty}"
                                        } else {
                                            ""
                                        }
                            } else {
                                ""
                            }
                } else {
                    ""
                }

        viewModel.savePrognosis(
            game.id.toString(),
            PrognosisModel(
                game.id,
                gameResult,
                coefficientForWin,
                coefficientForDraw,
                coefficientForDefeat,
                coefficientForFine,
            )
        )
    }

    private fun calcGamblerPlaceAndPoints(gamesId: ArrayList<Int>) {
        calcPointsAndPointsPrev(gamesId)

        calcPlace()

        calcPlacePrev(gamesId.size)
    }

    private fun calcStakePoints(stakesForGame: List<StakeModel>) {
        var place = 1
        var step = 0
        var points = 0.0

        stakesForGame.sortedWith(
            compareByDescending<StakeModel> { item -> item.points }
                .thenBy { el -> el.gamblerId }
        ).forEach { stake ->
            if (points == stake.points) {
                step++
            } else {
                place += step
                points = stake.points

                step = 1
            }

            stake.place = place
            stake.points = points

            viewModel.saveStake(stake)
        }
    }

    private fun calcPointsAndPointsPrev(gamesId: ArrayList<Int>) {
        viewModel.gamblers.value?.map { gambler ->
            val stakesForGambler = stakes.filter { item -> item.gamblerId == gambler.id && item.gameId in gamesId }

            gambler.points = ("%.2f".format(
                stakesForGambler.sumOf { stake -> stake.points }
            ).replace(",", ".")).toDouble()
            //gambler.points = ("%.2f".format(gambler.points).replace(",", ".")).toDouble()

            val size = stakesForGambler.size

            if (size > 1) {
                /*gambler.pointsPrev = stakesForGambler.sortedBy { item -> item.gameId }
                    .slice(0..stakesForGambler.size - 2)
                    .sumOf { stake -> stake.points }*/

                val lastGameId = stakesForGambler.maxByOrNull { item -> item.gameId }!!.gameId
                toLog("lastGameId: $lastGameId")
                val start = viewModel.games.value?.find { item -> item.id == lastGameId }?.start
                val firstGameId =
                    viewModel.games.value?.sortedBy { item -> item.id }?.find { item -> item.start == start }?.id ?: 0
                toLog("firstGameId: $firstGameId")
                gambler.pointsPrev = (stakesForGambler.filter { item -> item.gameId < firstGameId }
                    .sumOf { item -> item.points } * 100.0).roundToInt() / 100.0
            }

            toLog("pointsPrev: ${gambler.pointsPrev}")

            viewModel.saveGambler(gambler)
        }
    }

    private fun calcPlacePrev(gamesCount: Int) {
        var place = 1
        var step = 0
        var points = 0.0

        viewModel.gamblers.value?.sortedByDescending { item -> item.pointsPrev }?.forEach { gambler ->
            if (gamesCount == 1) {
                place = 0
            } else if (points == gambler.pointsPrev) {
                step++
            } else {
                place += step
                points = gambler.pointsPrev

                step = 1
            }

            gambler.placePrev = place

            viewModel.saveGambler(gambler)
        }
    }

    private fun calcPlace() {
        var place = 1
        var step = 0
        var points = 0.0

        viewModel.gamblers.value?.sortedWith(
            compareByDescending<GamblerModel> { item -> item.points }
                .thenBy { item -> item.nickname }
        )?.forEach { gambler ->
            if (points == gambler.points) {
                step++
            } else {
                place += step
                points = gambler.points

                step = 1
            }

            gambler.place = place

            viewModel.saveGambler(gambler)
        }
    }

    private fun calcPointsForAddTime(stake: StakeModel, game: GameModel) {
        stake.points += if (stake.goal1 == game.goal1 && stake.goal2 == game.goal2
            && stake.addGoal1 == game.addGoal1 && stake.addGoal2 == game.addGoal2
        ) {
            2.0
        } else if (game.addGoal1 != game.addGoal2
            && (stake.addGoal1.toInt() - stake.addGoal2.toInt()) == (game.addGoal1.toInt() - game.addGoal2.toInt())
        ) {
            1.25
        } else if (
            (game.addGoal1 > game.addGoal2 && stake.addGoal1 > stake.addGoal2)
            || (game.addGoal1 == game.addGoal2 && stake.addGoal1 == stake.addGoal2)
            || (game.addGoal1 < game.addGoal2 && stake.addGoal1 < stake.addGoal2)
        ) {
            1.0
        } else if (stake.addGoal1 == game.addGoal1 || stake.addGoal2 == game.addGoal2) {
            0.1
        } else {
            0.0
        }

        if (game.penalty.isNotBlank() && stake.penalty == game.penalty) {
            stake.points += 1
        }
    }

    private fun calcPoints(stake: StakeModel, game: GameModel, coefficient: Double, gamblersCount: Double): Double =
        if (stake.goal1 == game.goal1 && stake.goal2 == game.goal2) {
            val points = coefficient * 2
            if (points <= gamblersCount) points else coefficient
        } else if (game.goal1 != game.goal2
            && (game.goal1.toInt() - game.goal2.toInt()) == (stake.goal1.toInt() - stake.goal2.toInt())
        ) {
            coefficient * 1.25
        } else if (
            (game.goal1 > game.goal2 && stake.goal1 > stake.goal2)
            || (game.goal1 == game.goal2 && stake.goal1 == stake.goal2)
            || (game.goal1 < game.goal2 && stake.goal1 < stake.goal2)
        ) {
            if (stake.goal1 == game.goal1 || stake.goal2 == game.goal2) {
                coefficient * 1.1
            } else {
                coefficient
            }
        } else if (stake.goal1 == game.goal1 || stake.goal2 == game.goal2) {
            0.15
        } else {
            0.0
        }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController?.removeOnDestinationChangedListener(destinationListener)
        navController.addOnDestinationChangedListener(destinationListener)
        this.navController = navController
    }

    private fun prepareRootNavController(navController: NavController) {
        val graph = navController.navInflater.inflate(getMainNavigationGraphId())

        if (AppPreferences.getIsAuth()) {
            graph.setStartDestination(
                if (isProfileFilled(GAMBLER)) {
                    getTabsDestination()
                } else {
                    getProfileDestination()
                }
            )
        }

        if (graph.startDestinationId != this.navController?.graph?.startDestinationId) {
            navController.graph = graph
        }
    }

    private fun setCopyright() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val copyright = binding.mainFooter.copyrightYear

        if (year != YEAR_START) {
            val strYear = "$YEAR_START-$year"
            copyright.text = strYear
        } else {
            copyright.text = YEAR_START.toString()
        }
    }

    private fun getMainNavigationGraphId(): Int = R.navigation.main_graph

    private fun getMainDestination(): Int = R.id.startFragment
    private fun getTabsDestination(): Int = R.id.tabsFragment
    private fun getProfileDestination(): Int = R.id.profileFragment

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) return false

        val graph = destination.parent ?: return false

        val startDestinations = topLevelDestinations + graph.startDestinationId

        return startDestinations.contains(destination.id)
    }

    override fun onSupportNavigateUp(): Boolean = (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (isStartDestination(navController?.currentDestination)) {
            super.onBackPressed()
            /*onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        // back button pressed... finishing the activity
                        finish()
                    }
                })*/
        } else {
            navController?.popBackStack()
        }
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
        super.onDestroy()
    }
}