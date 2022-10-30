package com.example.tote_test.ui.admin.group_games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentAdminGroupGamesBinding
import com.example.tote_test.utils.APP_ACTIVITY

class AdminGroupGamesFragment : Fragment() {
    private lateinit var binding: FragmentAdminGroupGamesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminGroupGamesBinding.inflate(layoutInflater, container, false)

        val group = AdminGroupGamesFragmentArgs.fromBundle(requireArguments()).group

        APP_ACTIVITY.supportActionBar?.title = APP_ACTIVITY.getString(R.string.group, group)

        return binding.root
    }
}