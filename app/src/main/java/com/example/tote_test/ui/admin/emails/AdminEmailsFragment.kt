package com.example.tote_test.ui.admin.emails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentAdminEmailsBinding
import com.example.tote_test.models.EmailModel
import com.example.tote_test.ui.main.MainViewModel
import com.example.tote_test.utils.findTopNavController

class AdminEmailsFragment : Fragment() {
    private lateinit var binding: FragmentAdminEmailsBinding
    private val viewModelEmail: MainViewModel by viewModels()
    private val adapter = AdminEmailsAdapter { email -> onListItemClick(email) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminEmailsBinding.inflate(layoutInflater, container, false)

        binding.adminEmailsDataAbsent.isGone = true

        binding.adminEmailsAddEmail
        binding.adminEmailsAddEmail.setOnClickListener {
            findTopNavController().navigate(R.id.action_adminEmailsFragment_to_adminEmailFragment)
        }

        val recyclerView = binding.adminEmailsList
        recyclerView.adapter = adapter

        observeEmails()

        return binding.root
    }

    private fun onListItemClick(email: EmailModel) {
        /*val action = if (GAMBLER.admin) {
            RatingFragmentDirections.actionRatingFragmentToAdminGamblerFragment(gambler)
        } else {
            RatingFragmentDirections.actionRatingFragmentToAdminGamblerPhotoFragment(gambler.photoUrl, true)
        }
        findTopNavController().navigate(action)*/
    }

    private fun observeEmails() = viewModelEmail.emails.observe(viewLifecycleOwner) {
        binding.adminEmailsProgressBar.isVisible = true

        binding.adminEmailsDataAbsent.isGone = it.isNotEmpty()

        adapter.setEmails(it)

        binding.adminEmailsProgressBar.isVisible = false
    }
}