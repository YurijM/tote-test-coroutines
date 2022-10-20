package com.example.tote_test.ui.tabs.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.databinding.FragmentGamesBinding
import com.example.tote_test.models.GameModel

class GamesFragment : Fragment() {
    private lateinit var binding: FragmentGamesBinding
    private lateinit var viewModel: GamesViewModel
    private val adapter = GamesAdapter { game -> onListItemClick(game) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]

        binding = FragmentGamesBinding.inflate(layoutInflater, container, false)

        /*val recyclerView = binding.ratingList
        recyclerView.adapter = adapter*/

        return binding.root
    }

    private fun onListItemClick(game: GameModel) {

    }
}