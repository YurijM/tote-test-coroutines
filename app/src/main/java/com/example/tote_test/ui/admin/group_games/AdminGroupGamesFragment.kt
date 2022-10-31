package com.example.tote_test.ui.admin.group_games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentAdminGroupGamesBinding
import com.example.tote_test.utils.APP_ACTIVITY

class AdminGroupGamesFragment : Fragment() {
    private lateinit var binding: FragmentAdminGroupGamesBinding
    //private val viewModel: AdminGroupGamesViewModel by viewModels()
    private lateinit var viewModel: AdminGroupGamesViewModel
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

        viewModel = ViewModelProvider(this)[AdminGroupGamesViewModel::class.java]
        observeGames()

        adapter.setOnItemClickListener {
            viewModel.saveGame(it)
        }

        return binding.root
    }

    private fun observeGames() = viewModel.games.observe(viewLifecycleOwner) {
        binding.adminGroupGamesProgressBar.isVisible = true

        val games = it
            .filter { item -> item.group == group }
            .sortedBy { item -> item.id }

        binding.adminGroupGamesProgressBar.isInvisible = true

        adapter.setGames(games)
    }
}