package com.example.tote_test.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentSignUpBinding
import com.example.tote_test.utils.*

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object {}.javaClass.enclosingMethod?.name}")

        /*APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY.supportActionBar?.setDisplayShowHomeEnabled(true)*/

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        var isEmailFilled = false
        var isPasswordFilled = false
        var isPasswordConfirmFilled = false

        observeStatus()

        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        binding.signUpInputEmail.addTextChangedListener {
            if (it != null) {
                isEmailFilled = !checkFieldBlank(it.toString(), binding.signUpLayoutInputEmail, getString(R.string.email))

                binding.signUpToRegistration.isEnabled = isEmailFilled && isPasswordFilled && isPasswordConfirmFilled
            }
        }

        binding.signUpInputPassword.addTextChangedListener {
            if (it != null) {
                if (!checkFieldBlank(it.toString(), binding.signUpLayoutInputPassword, getString(R.string.password))) {
                    isPasswordFilled = checkMinLength(MIN_PASSWORD_LENGTH, it, binding.signUpLayoutInputPassword, getString(R.string.password))
                }

                binding.signUpToRegistration.isEnabled = isEmailFilled && isPasswordFilled && isPasswordConfirmFilled
            }
        }

        binding.signUpInputPasswordConfirm.addTextChangedListener {
            if (it != null) {
                isPasswordConfirmFilled = !checkFieldBlank(it.toString(), binding.signUpLayoutInputPasswordConfirm, getString(R.string.password_confirm))

                binding.signUpToRegistration.isEnabled = isEmailFilled && isPasswordFilled && isPasswordConfirmFilled
            }
        }

        binding.signUpToRegistration.setOnClickListener {
            val password = binding.signUpInputPassword.text.toString().trim()
            val passwordConfirm = binding.signUpInputPasswordConfirm.text.toString().trim()

            if (comparePassword(password, passwordConfirm)) {
                EMAIL = binding.signUpInputEmail.text.toString().trim()
                PASSWORD = password

                viewModel.signUp()
            }
        }

        return binding.root
    }

    private fun comparePassword(password: String, confirmPassword: String): Boolean {
        var result = false

        if (password != confirmPassword) {
            binding.signUpLayoutInputPasswordConfirm.isErrorEnabled = true
            binding.signUpLayoutInputPasswordConfirm.error = getString(R.string.error_confirm_password)
        } else {
            binding.signUpLayoutInputPasswordConfirm.isErrorEnabled = false
            result = true
        }

        return result
    }

    private fun observeStatus() = viewModel.status.observe(viewLifecycleOwner) {
        when (it) {
            is Resource.Loading -> {
                binding.signUpProgressBar.isVisible = true
            }
            is Resource.Success -> {
                binding.signUpProgressBar.isInvisible = true

                if (CURRENT_ID.isNotBlank()) {
                    AppPreferences.setIsAuth(true)
                    findTopNavController().navigate(R.id.action_signUpFragment_to_profileFragment)
                }
            }
            is Resource.Error -> {
                binding.signUpProgressBar.isInvisible = true
                fixError(it.message.toString())
            }
        }
    }
}