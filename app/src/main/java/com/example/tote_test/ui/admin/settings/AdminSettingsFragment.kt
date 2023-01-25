package com.example.tote_test.ui.admin.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentAdminSettingsBinding
import com.example.tote_test.models.StakeModel
import com.example.tote_test.ui.main.MainViewModel
import com.example.tote_test.utils.*

class AdminSettingsFragment : Fragment() {
    private val none = -1
    private val schedule = 0
    private val stakes = 1

    private lateinit var binding: FragmentAdminSettingsBinding
    private val viewModel: AdminSettingsViewModel by viewModels()
    private val viewModelMain: MainViewModel by viewModels()
    private var functionCode = none

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        //viewModel = ViewModelProvider(this)[AdminSettingsViewModel::class.java]

        binding = FragmentAdminSettingsBinding.inflate(layoutInflater, container, false)

        observeStatus()

        binding.adminSettingsEmail.setOnClickListener {
            findTopNavController().navigate(R.id.action_adminSettingsFragment_to_adminEmailsFragment)
        }

        binding.adminSettingsAddGames.setOnClickListener {
            addGames()
        }

        binding.adminSettingsRandomStakes.setOnClickListener {
            saveRandomStakes()
        }

        /*binding.adminSettingsAddTeams.setOnClickListener {
            addTeams()
        }*/

        return binding.root
    }

    private fun addGames() {
        functionCode = schedule

        GAMES.forEach {
            viewModel.addGame(it)
        }
    }

    private fun saveRandomStakes() {
        functionCode = stakes

        viewModelMain.games.value?.forEach { game ->
            viewModelMain.gamblers.value?.forEach { gambler ->
                val randomGoal1 = (0..3).random()
                val randomGoal2 = (0..3).random()

                val stake = StakeModel()
                with(stake) {
                    gameId = game.id
                    gamblerId = gambler.id
                    goal1 = randomGoal1.toString()
                    goal2 = randomGoal2.toString()
                }

                viewModel.saveStake(stake)
            }
        }
    }

    /*private fun addTeams() {
        functionCode = teams

        TEAMS.forEach {
            viewModel.addTeam(it)
        }
    }*/

    override fun onStart() {
        super.onStart()

        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun observeStatus() = viewModel.status.observe(viewLifecycleOwner) {
        toLog("functionCode: $functionCode")

        when (it) {
            is Resource.Loading -> {
                binding.adminSettingsProgressBar.isVisible = true
            }
            is Resource.Success -> {
                var success = ""
                var error = ""
                var errorLoad = false

                when (functionCode) {
                    schedule -> {
                        success = resources.getString(R.string.admin_settings_add_games_success)
                        error = resources.getString(R.string.error_add_games)
                    }
                    stakes -> {
                        success = resources.getString(R.string.admin_settings_random_stakes_success)
                        error = resources.getString(R.string.error_random_stakes)
                    }
                }

                if (it.data == false) errorLoad = true

                binding.adminSettingsProgressBar.isInvisible = true

                showToast(
                    if (!errorLoad) {
                        success
                    } else {
                        error
                    }
                )
            }
            is Resource.Error -> {
                binding.adminSettingsProgressBar.isInvisible = true

                showToast(it.message.toString())
                fixError(it.message.toString())
            }
        }
    }
}