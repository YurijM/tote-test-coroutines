package com.example.tote_test.ui.admin.emails.email

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentAdminEmailBinding
import com.example.tote_test.models.EmailModel
import com.example.tote_test.ui.main.MainViewModel
import com.example.tote_test.utils.Resource
import com.example.tote_test.utils.findTopNavController
import com.example.tote_test.utils.fixError

class AdminEmailFragment : Fragment() {
    private lateinit var binding: FragmentAdminEmailBinding
    private val viewModel: AdminEmailViewModel by viewModels()
    private val viewModelEmail: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminEmailBinding.inflate(layoutInflater, container, false)

        val email = AdminEmailFragmentArgs.fromBundle(requireArguments()).email

        initEmail(email)

        observeStatus()

        binding.adminEmailSave.setOnClickListener {
            if (binding.adminEmailInputEmail.text.toString().isNotBlank()) {

                email.email = binding.adminEmailInputEmail.text.toString()

                viewModel.saveEmail(email)
            }
        }

        binding.adminEmailDelete.setOnClickListener {
            val dialog: AlertDialog = AlertDialog.Builder(requireContext())
                .setMessage("Вы действительно хотите удалить адрес ${email.email} ?")
                .setPositiveButton("Да") {dialog, _ ->
                    viewModel.removeEmail(email.id.toString())
                    dialog.cancel()
                }
                .setNegativeButton("Нет") {dialog, _ ->
                    dialog.cancel()
                }
                .create()
            dialog.show()
        }

        return binding.root
    }

    private fun initEmail(email: EmailModel) {
        binding.adminEmailInputEmail.setText(email.email)

        if (email.id == 0) {
            val emails: List<EmailModel> = (viewModelEmail.emails.value ?: emptyList())
            val id = (emails.maxOfOrNull { item -> item.id } ?: 0) + 1

            email.id = id

            binding.adminEmailDelete.isGone = true
        } else {
            binding.adminEmailDelete.isGone = false
        }

    }

    private fun observeStatus() = viewModel.status.observe(viewLifecycleOwner) {
        when (it) {
            is Resource.Loading -> {
                binding.adminEmailProgressBar.isVisible = true
            }
            is Resource.Success -> {
                binding.adminEmailProgressBar.isInvisible = true
                findTopNavController().navigate(R.id.action_adminEmailFragment_to_adminEmailsFragment)
            }
            is Resource.Error -> {
                binding.adminEmailProgressBar.isInvisible = true
                fixError(it.message.toString())
            }
        }
    }

}