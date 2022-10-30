package com.example.tote_test.ui.admin.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentAdminSettingsBinding
import com.example.tote_test.utils.*

class AdminSettingsFragment : Fragment() {
    private lateinit var binding: FragmentAdminSettingsBinding
    private lateinit var viewModel: AdminSettingsViewModel
    private var errorLoad = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        viewModel = ViewModelProvider(this)[AdminSettingsViewModel::class.java]

        binding = FragmentAdminSettingsBinding.inflate(layoutInflater, container, false)

        observeStatus()

        binding.adminSettingsAddGames.setOnClickListener {
            errorLoad = false

            GAMES.forEach {
                viewModel.addGame(it)
            }

            showToast(
                if (!errorLoad) {
                    APP_ACTIVITY.resources.getString(R.string.admin_settings_add_games_success)
                } else {
                    APP_ACTIVITY.resources.getString(R.string.error_add_games)
                }
            )
        }

        binding.adminSettingsAddTeams.setOnClickListener {
            errorLoad = false

            TEAMS.forEach {
                viewModel.addTeam(it)
            }

            showToast(
                if (!errorLoad) {
                    APP_ACTIVITY.resources.getString(R.string.admin_settings_add_teams_success)
                } else {
                    APP_ACTIVITY.resources.getString(R.string.error_add_teams)
                }
            )
        }
        return binding.root
    }

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
                if (it.data == false) errorLoad = true
                binding.adminSettingsProgressBar.isInvisible = true
            }
            is Resource.Error -> {
                binding.adminSettingsProgressBar.isInvisible = true
                fixError(it.message.toString())
            }
        }
    }
}