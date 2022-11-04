package com.example.tote_test.ui.tabs.stakes

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
import java.util.*

class StakesFragment : Fragment() {
    private lateinit var binding: FragmentStakesBinding
    private val viewModelGames: MainViewModel by viewModels()
    private val viewModel: StakesViewModel by viewModels()
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

    private fun observeGames() = viewModelGames.games.observe(viewLifecycleOwner) {
        binding.stakesProgressBar.isVisible = true

        val now = Calendar.getInstance().time.time
        games = it.filter { item -> now < item.start.toLong() }
            .sortedBy { item -> item.id }

        games.forEach { game ->
            val stake = stakes.filter { item -> item.gameId == game.id }

            if (stake.isNotEmpty()) {
                game.goal1 = stake[0].goal1
                game.goal2 = stake[0].goal2
            } else {
                game.goal1 = ""
                game.goal2 = ""
            }
        }

        binding.stakesProgressBar.isInvisible = true

        adapter.setStakes(games)
    }

    private fun observeStakes() = viewModel.stakes.observe(viewLifecycleOwner) {
        stakes = it.filter { item -> item.gamblerId == CURRENT_ID }

        observeGames()
    }

    private fun onListItemClick(game: GameModel) {
        findNavController().navigate(StakesFragmentDirections.actionStakesFragmentToStakeFragment(game))
    }
}