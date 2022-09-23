package com.example.tote_test.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
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
import com.example.tote_test.ui.tabs.TabsFragment
import com.example.tote_test.utils.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    private val viewModel: MainViewModel by viewModels()

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
        toLog("destinationListener: $destination")
        supportActionBar?.title = destination.label
        supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        APP_ACTIVITY = this

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        if (CURRENT_ID.isNotBlank()) loadAppBarPhoto()

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
        menu.findItem(R.id.menu_item_admin).isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_exit -> {
                viewModel.signOut()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
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