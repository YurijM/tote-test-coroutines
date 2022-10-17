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
import com.example.tote_test.utils.Resource
import com.example.tote_test.utils.SCHEDULER
import com.example.tote_test.utils.TEAMS
import com.example.tote_test.utils.fixError

class GamesFragment : Fragment() {
    private lateinit var binding: FragmentGamesBinding
    private lateinit var viewModel: GamesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]

        binding = FragmentGamesBinding.inflate(layoutInflater, container, false)

        observeStatus()

        binding.gamesAddGame.setOnClickListener {
            SCHEDULER.forEach {
                viewModel.addGame(it)
            }
        }

        binding.gamesAddTeam.setOnClickListener {
            TEAMS.forEach {
                viewModel.addTeam(it)
            }
        }

        return binding.root
    }

    private fun observeStatus() = viewModel.status.observe(viewLifecycleOwner) {
        when (it) {
            is Resource.Loading -> {
                binding.gamesProgressBar.isVisible = true
            }
            is Resource.Success -> {
                binding.gamesProgressBar.isInvisible = true
            }
            is Resource.Error -> {
                binding.gamesProgressBar.isInvisible = true
                fixError(it.message.toString())
            }
        }
    }
}