package com.example.tote_test.ui.admin.group_games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentAdminGroupGamesBinding
import com.example.tote_test.ui.main.MainViewModel
import com.example.tote_test.utils.APP_ACTIVITY
import com.example.tote_test.utils.Resource
import com.example.tote_test.utils.fixError
import com.example.tote_test.utils.toLog

class AdminGroupGamesFragment : Fragment() {
    private lateinit var binding: FragmentAdminGroupGamesBinding
    private val viewModelGames: MainViewModel by viewModels()
    private val viewModel: AdminGroupGamesViewModel by viewModels()
    //private lateinit var viewModel: AdminGroupGamesViewModel
    private val adapter = AdminGroupGamesAdapter()
    private var group = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminGroupGamesBinding.inflate(layoutInflater, container, false)

        val recyclerView = binding.adminGroupGamesList
        recyclerView.adapter = adapter

        group = AdminGroupGamesFragmentArgs.fromBundle(requireArguments()).group

        APP_ACTIVITY.supportActionBar?.title = APP_ACTIVITY.getString(R.string.group, group)

        //viewModel = ViewModelProvider(this)[AdminGroupGamesViewModel::class.java]
        //viewModelGames = APP_ACTIVITY.viewModel //ViewModelProvider(this)[MainViewModel::class.java]

        observeGames()
        observeStatus()

        adapter.setOnItemClickListener {
            viewModel.saveGame(it)
        }

        return binding.root
    }

    private fun observeGames() = viewModelGames.games.observe(viewLifecycleOwner) {
        binding.adminGroupGamesProgressBar.isVisible = true

        val games = it
            .filter { item -> item.group == group }
            .sortedBy { item -> item.id }

        binding.adminGroupGamesProgressBar.isInvisible = true

        adapter.setGames(games)
    }

    private fun observeStatus() = viewModel.status.observe(viewLifecycleOwner) {
        when (it) {
            is Resource.Loading -> {
                binding.adminGroupGamesProgressBar.isVisible = true
            }
            is Resource.Success -> {
                binding.adminGroupGamesProgressBar.isInvisible = true
            }
            is Resource.Error -> {
                binding.adminGroupGamesProgressBar.isInvisible = true
                fixError(it.message.toString())
            }
        }
    }
}