package com.example.tote_test.ui.tabs.stakes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tote_test.databinding.FragmentStakesBinding
import com.example.tote_test.models.GameModel
import com.example.tote_test.models.StakeModel
import com.example.tote_test.ui.main.MainViewModel
import com.example.tote_test.utils.CURRENT_ID
import com.example.tote_test.utils.asTime
import java.text.SimpleDateFormat
import java.util.*

class StakesFragment : Fragment() {
    private lateinit var binding: FragmentStakesBinding
    private val viewModel: MainViewModel by viewModels()
    private val adapter = StakesAdapter { game -> onListItemClick(game) }
    private lateinit var games: List<GameModel>
    private lateinit var stakes: List<StakeModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        binding = FragmentStakesBinding.inflate(layoutInflater, container, false)

        val recyclerView = binding.stakesList
        recyclerView.adapter = adapter

        observeStakes()

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun observeGames() = viewModel.games.observe(viewLifecycleOwner) {
        binding.stakesProgressBar.isVisible = true

        val now = Calendar.getInstance().time.time
        val nowLocale = SimpleDateFormat("dd.MM.yyyy HH:mm").parse(now.toString().asTime(toLocale = true))?.time ?: 0

        /*val gamesPlayOff = it.filter { item ->
            val isPlayOff = GROUPS.any { group -> group.group == item.group && group.number > GROUPS_COUNT }
            isPlayOff && (now < item.start.toLong())
        }.sortedByDescending { item -> item.id }

        val gamesGroup = it.filter { item ->
            val isGroup = GROUPS.any { group -> group.group == item.group && group.number <= GROUPS_COUNT }
            isGroup && now < item.start.toLong()
        }.sortedBy { item -> item.id }

        games = gamesPlayOff + gamesGroup*/

        games = it.filter { item -> nowLocale < item.start.toLong() }

        games.forEach { game ->
            val stake = stakes.find { item -> item.gameId == game.id }

            //if (stake.isNotEmpty()) {
            if (stake != null) {
                game.goal1 = stake.goal1
                game.goal2 = stake.goal2
                game.addGoal1 = stake.addGoal1
                game.addGoal2 = stake.addGoal2
                game.penalty = stake.penalty
            } else {
                game.goal1 = ""
                game.goal2 = ""
            }
        }

        binding.stakesProgressBar.isInvisible = true

        adapter.setStakes(games)
    }

    private fun observeStakes() = viewModel.stakesAll.observe(viewLifecycleOwner) {
        stakes = it.filter { item -> item.gamblerId == CURRENT_ID }

        observeGames()
    }

    private fun onListItemClick(game: GameModel) {
        findNavController().navigate(StakesFragmentDirections.actionStakesFragmentToStakeFragment(game))
    }
}