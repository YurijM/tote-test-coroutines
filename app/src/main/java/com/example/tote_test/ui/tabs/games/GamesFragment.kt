package com.example.tote_test.ui.tabs.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.databinding.FragmentGamesBinding
import com.example.tote_test.models.GameModel
import com.example.tote_test.models.GroupGamesModel
import com.example.tote_test.models.GroupModel
import com.example.tote_test.models.TeamModel
import com.example.tote_test.utils.*

class GamesFragment : Fragment() {
    private lateinit var binding: FragmentGamesBinding
    private lateinit var viewModel: GamesViewModel
    private lateinit var groups: List<GroupModel>
    private lateinit var teams: List<TeamModel>
    private val adapter = GamesAdapter { group -> onListItemClick(group) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]

        binding = FragmentGamesBinding.inflate(layoutInflater, container, false)

        val recyclerView = binding.groupGamesList
        recyclerView.adapter = adapter

        groups = GROUPS.filter { it.number <= GROUPS_COUNT }
            .sortedBy { it.number }

        observeGames()

        return binding.root
    }

    private fun observeGames() = viewModel.games.observe(viewLifecycleOwner) {
        binding.gamesProgressBar.isVisible = true

        val games = arrayListOf<GroupGamesModel>()

        groups.forEach { group ->
            teams = TEAMS.filter { team -> team.group == group.group }
                .sortedBy { item -> item.team }

            val groupGames = it.filter { item -> item.group == group.group }.toMutableList()

            val groupGamesByTeam = arrayListOf<GameModel>()

            teams.forEach {
                groupGames.filter { item -> item.team1 == it.team || item.team2 == it.team }
                    .sortedBy { sort -> if (sort.team1 == it.team) sort.team2 else sort.team1 }
                    .forEach { game ->
                        groupGamesByTeam.add(game)
                        groupGames.remove(game)
                    }
            }

            games.add(
                GroupGamesModel(
                    group.group,
                    groupGamesByTeam
                )
            )
        }

        binding.gamesProgressBar.isInvisible = true

        adapter.setGames(games)
    }

    private fun onListItemClick(group: String) {
        if (GAMBLER.admin) {
            findTopNavController().navigate(GamesFragmentDirections.actionGamesFragmentToAdminGroupGamesFragment(group))
        }
    }
}