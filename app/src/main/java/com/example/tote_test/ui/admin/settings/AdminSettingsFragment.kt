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
    private val noneCode = -1
    private val scheduleCode = 0
    private val stakesCode = 1

    private data class AdminAction(
        val code: Int,
        val messageSuccess: String = "",
        val messageError: String = ""
    )

    private var adminActions = arrayListOf(
        AdminAction(
            code = scheduleCode,
            messageSuccess = APP_ACTIVITY.resources.getString(R.string.admin_settings_add_games_success),
            messageError = APP_ACTIVITY.resources.getString(R.string.error_add_games)
        ),
        AdminAction(
            code = stakesCode,
            messageSuccess = APP_ACTIVITY.resources.getString(R.string.admin_settings_random_stakes_success),
            messageError = APP_ACTIVITY.resources.getString(R.string.error_random_stakes)
        )
    )

    private lateinit var binding: FragmentAdminSettingsBinding
    private val viewModel: AdminSettingsViewModel by viewModels()
    private val viewModelMain: MainViewModel by viewModels()

    private var actionCode = noneCode

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
        actionCode = scheduleCode

        val size = viewModelMain.games.value?.size ?: 0

        GAMES.forEachIndexed { index, game ->
            viewModel.addGame(game, size <= (index + 1))
        }
    }

    private fun saveRandomStakes() {
        actionCode = stakesCode

        val size = (viewModelMain.games.value?.size ?: 0) * (viewModelMain.gamblers.value?.size ?: 0)

        viewModelMain.games.value?.forEachIndexed { i, game ->
            viewModelMain.gamblers.value?.forEachIndexed { j, gambler ->
                val randomGoal1 = (0..3).random()
                val randomGoal2 = (0..3).random()

                val stake = StakeModel(
                    gameId = game.id,
                    gamblerId = gambler.id,
                    goal1 = randomGoal1.toString(),
                    goal2 = randomGoal2.toString()
                )

                viewModel.saveStake(stake, size <= (i + 1) * (j + 1))
            }
        }
    }

    /*private fun addTeams() {
        actionCode = teams

        TEAMS.forEach {
            viewModel.addTeam(it)
        }
    }*/

    override fun onStart() {
        super.onStart()

        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun observeStatus() = viewModel.status.observe(viewLifecycleOwner) {
        when (it) {
            is Resource.Loading -> {
                binding.adminSettingsProgressBar.isVisible = true
            }
            is Resource.Success -> {
                val success = adminActions.find { action -> action.code == actionCode }?.messageSuccess ?: ""
                val error = adminActions.find { action -> action.code == actionCode }?.messageError ?: ""
                val errorLoad = it.data == false

                binding.adminSettingsProgressBar.isInvisible = true

                showToast(
                    if (!errorLoad) success else error
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