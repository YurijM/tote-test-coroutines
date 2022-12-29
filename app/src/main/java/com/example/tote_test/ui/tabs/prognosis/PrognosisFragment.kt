package com.example.tote_test.ui.tabs.prognosis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tote_test.databinding.FragmentPrognosisBinding
import com.example.tote_test.models.GameModel
import com.example.tote_test.models.GameStakesModel
import com.example.tote_test.models.StakeModel
import com.example.tote_test.ui.main.MainViewModel
import com.example.tote_test.utils.toLog
import java.util.*

class PrognosisFragment : Fragment() {
    private lateinit var binding: FragmentPrognosisBinding
    private val viewModelMain: MainViewModel by viewModels()
    private val viewModel: PrognosisViewModel by viewModels()
    private lateinit var games: List<GameModel>
    private val adapter = PrognosisAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        binding = FragmentPrognosisBinding.inflate(layoutInflater, container, false)

        val recyclerView = binding.prognosisList
        recyclerView.adapter = adapter

        observeGames()

        return binding.root
    }

    private fun observePrognosis() = viewModel.prognosis.observe(viewLifecycleOwner) {
        val prognosis = arrayListOf<GameStakesModel>()

        val gamesForStakes = games.sortedByDescending { item -> item.id }

        gamesForStakes.forEach { game ->
            val stakes = arrayListOf<StakeModel>()

            it.filter { item -> item.gameId == game.id }.forEach { stake ->
                val gambler = viewModel.gamblers.value?.find { gambler -> gambler.id == stake.gamblerId }
                if (gambler != null) {
                    stake.gamblerId = gambler.nickname
                }

                stakes.add(stake)
            }

            val gamblersCount = (viewModel.gamblers.value?.size ?: 0).toDouble()

            val stakesWinCount = stakes.filter { it.goal1.isNotBlank() && it.goal1 > it.goal2 }.size
            val stakesDrawCount = stakes.filter { it.goal1.isNotBlank() && it.goal1 == it.goal2 }.size
            val stakesDefeatCount = stakes.filter { it.goal1.isNotBlank() && it.goal1 < it.goal2 }.size

            val coefficientForWin = if (stakesWinCount > 0) gamblersCount / stakesWinCount else 0.0
            val coefficientForDraw = if (stakesDrawCount > 0) gamblersCount / stakesDrawCount else 0.0
            val coefficientForDefeat = if (stakesDefeatCount > 0) gamblersCount / stakesDefeatCount else 0.0
            val coefficientForFine = -((coefficientForWin + coefficientForDraw + coefficientForDefeat) / 3)

            stakes.forEach { stake ->
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

            prognosis.add(
                GameStakesModel(
                    gameResult,
                    coefficientForWin,
                    coefficientForDraw,
                    coefficientForDefeat,
                    coefficientForFine,
                    stakes.sortedBy { it.gamblerId }
                )
            )

            var place = 1
            var step = 0
            var points = 0.0

            stakes.sortedWith(
                compareByDescending<StakeModel> { it.points }
                    .thenBy { it.gamblerId }
            ).forEach { stake ->
                val gambler = viewModel.gamblers.value?.find { gambler -> gambler.nickname == stake.gamblerId }
                if (gambler != null) {
                    if (gambler.place > 0) gambler.placePrev = gambler.place

                    gambler.points = stake.points

                    toLog("points - $points, stake.points = ${stake.points}")
                    toLog("step before = $step")

                    if (points == stake.points) {
                        step++
                    } else {
                        place += step
                        points = stake.points

                        step = 1
                    }

                    toLog("step after = $step")

                    gambler.place = place

                    viewModel.saveGambler(gambler)
                }
            }
        }

        adapter.setPrognosis(prognosis)
    }

    private fun observeGames() = viewModelMain.games.observe(viewLifecycleOwner) {
        val now = Calendar.getInstance().time.time

        games = it.filter { item -> now > item.start.toLong() }

        if (games.isNotEmpty()) {
            binding.prognosisTournamentNotStarted.isGone = true

            observePrognosis()
        } else {
            binding.prognosisTournamentNotStarted.text = "Турнир ещё не начался"
            binding.prognosisTournamentNotStarted.isGone = false
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
}