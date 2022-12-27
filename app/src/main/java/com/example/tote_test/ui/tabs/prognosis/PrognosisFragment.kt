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
                } else if (stake.goal1 == game.goal1 && stake.goal2 == game.goal2) {
                    if (coefficient < gamblersCount) coefficient * 2 else coefficient
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

        //observeGames()
    }
}