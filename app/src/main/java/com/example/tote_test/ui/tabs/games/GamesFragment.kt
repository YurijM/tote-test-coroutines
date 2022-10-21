package com.example.tote_test.ui.tabs.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.databinding.FragmentGamesBinding
import com.example.tote_test.models.GameModel
import com.example.tote_test.models.GroupGamesModel
import com.example.tote_test.utils.GROUPS
import com.example.tote_test.utils.GROUPS_COUNT
import com.example.tote_test.utils.TEAMS
import com.example.tote_test.utils.toLog

class GamesFragment : Fragment() {
    private lateinit var binding: FragmentGamesBinding
    private lateinit var viewModel: GamesViewModel

    //private val adapter = GamesAdapter { game -> onListItemClick(game) }
    private val adapter = GamesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]

        binding = FragmentGamesBinding.inflate(layoutInflater, container, false)

        val recyclerView = binding.groupGamesList
        recyclerView.adapter = adapter

        val groups = GROUPS.filter { it -> it.number <= GROUPS_COUNT }
            .sortedBy { it.number }

        /*val groupGames = viewModel.games.value//.filter { it -> it.group == "A" }
        toLog("groupGames: $groupGames")*/


        observeGames()

        return binding.root
    }

    private fun onListItemClick(game: GameModel) {

    }

    private fun observeGames() = viewModel.games.observe(viewLifecycleOwner) {
        val groupGames = arrayListOf<GroupGamesModel>()

        val teams = TEAMS.filter { it -> it.group == "A" }
            .sortedBy { item -> item.team }

        groupGames.add(GroupGamesModel(
            "A",
            it.filter { it -> it.group == "A" }
                /*.sortedBy { item -> (item.team1 == teams[0].team || item.team2 == teams[0].team) }
                .sortedBy { item -> (item.team1 == teams[1].team || item.team2 == teams[1].team) }
                .sortedBy { item -> (item.team1 == teams[2].team || item.team2 == teams[2].team) }
                .sortedBy { item -> (item.team1 == teams[3].team || item.team2 == teams[3].team) }.toList()*/
                .sortedWith(
                    compareBy<GameModel> { item -> (item.team1 == teams[0].team || item.team2 == teams[0].team) }
                        .thenBy { item -> (item.team1 == teams[1].team || item.team2 == teams[1].team) }
                        .thenBy { item -> (item.team1 == teams[2].team || item.team2 == teams[2].team) }
                        .thenBy { item -> (item.team1 == teams[3].team || item.team2 == teams[3].team) }
                )
        ))
        //groupGames.add(GroupGamesModel("B", it.filter { item -> item.group == "B" }))
        toLog("groupGames: $groupGames")

        /*val games = arrayListOf<GameModel>()
        games.add(it[0])*/
        adapter.setGames(groupGames)
    }
}