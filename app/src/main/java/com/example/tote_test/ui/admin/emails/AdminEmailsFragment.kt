package com.example.tote_test.ui.admin.emails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
            findTopNavController().navigate(
                AdminEmailsFragmentDirections.actionAdminEmailsFragmentToAdminEmailFragment(EmailModel())
            )
        }

        val recyclerView = binding.adminEmailsList
        recyclerView.adapter = adapter

        observeEmails()

        return binding.root
    }

    private fun onListItemClick(email: EmailModel) {
        findTopNavController().navigate(AdminEmailsFragmentDirections.actionAdminEmailsFragmentToAdminEmailFragment(email))
    }

    private fun observeEmails() = viewModelEmail.emails.observe(viewLifecycleOwner) {
        binding.adminEmailsProgressBar.isVisible = true

        binding.adminEmailsDataAbsent.isGone = it.isNotEmpty()

        val emails = it.sortedBy { item -> item.email }

        adapter.setEmails(emails)

        binding.adminEmailsProgressBar.isVisible = false
    }
}