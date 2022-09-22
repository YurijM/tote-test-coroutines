package com.example.tote_test.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentSignInBinding
import com.example.tote_test.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()

    private var isEmailFilled = false
    private var isPasswordFilled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        //viewModel = ViewModelProvider(this)[SignInViewModel::class.java]

        //observeGambler()

        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        initFields()

        observeStatus()

        binding.signInToAuth.isEnabled = isFieldsFilled()

        binding.signInToAuth.setOnClickListener {
            login()
        }

        return binding.root
    }

    private fun initFields() {
        initFieldEmail()
        initFieldPassword()
    }

    private fun initFieldEmail() {
        binding.signInInputEmail.addTextChangedListener {
            if (it != null) {
                isEmailFilled = !checkFieldBlank(it.toString(), binding.signInLayoutInputEmail, getString(R.string.email))

                binding.signInToAuth.isEnabled = isEmailFilled && isPasswordFilled
            }
        }
    }

    private fun initFieldPassword() {
        binding.signInInputPassword.addTextChangedListener {
            if (it != null) {
                isPasswordFilled =
                    !checkFieldBlank(it.toString(), binding.signInLayoutInputPassword, getString(R.string.password))

                binding.signInToAuth.isEnabled = isFieldsFilled()
            }
        }
    }

    private fun isFieldsFilled(): Boolean = isEmailFilled && isPasswordFilled

    private fun login() {
        EMAIL = binding.signInInputEmail.text.toString().trim()
        PASSWORD = binding.signInInputPassword.text.toString().trim()

        viewModel.signIn()

        /*viewModel.signIn() {
            toLog("SignInFragment -> login -> GAMBLER: $GAMBLER")
            AppPreferences.setIsAuth(true)
            loadAppBarPhoto()

            if (isProfileFilled(GAMBLER)) {
                findTopNavController().navigate(R.id.action_signInFragment_to_tabsFragment)
            } else {
                findTopNavController().navigate(R.id.action_signInFragment_to_profileFragment)
            }
        }*/
    }

    private fun observeStatus() = viewModel.status.observe(viewLifecycleOwner) {
        when (it) {
            is Resource.Loading -> {
                binding.signInProgressBar.isVisible = true
            }
            is Resource.Success -> {
                binding.signInProgressBar.isVisible = false

                CURRENT_ID = AUTH.currentUser?.uid.toString()
                if (CURRENT_ID.isNotBlank()) {
                    AppPreferences.setIsAuth(true)

                    lifecycleScope.launch(Dispatchers.IO) {
                        GAMBLER = REPOSITORY.getGambler(CURRENT_ID)

                        withContext(Dispatchers.Main) {
                            loadAppBarPhoto()
                            toLog("GAMBLER: $GAMBLER")

                            if (isProfileFilled(GAMBLER)) {
                                //findTopNavController().navigate(R.id.action_signInFragment_to_tabsFragment)
                            } else {
                                findTopNavController().navigate(R.id.action_signInFragment_to_profileFragment)
                            }
                        }
                    }
                }
            }
            is Resource.Error -> {
                binding.signInProgressBar.isVisible = false
                fixError(it.message.toString())
            }
        }
    }

    /*private fun observeGambler() = viewModel.gambler.observe(viewLifecycleOwner) {
        toLog("SignInFragment -> observeGambler -> GAMBLER: $GAMBLER")
        GAMBLER = it
        toLog("SignInFragment -> observeGambler -> GAMBLER after: $GAMBLER")

        *//*if (GAMBLER.id.isNotBlank()) {
            AppPreferences.setIsAuth(true)

            if (isProfileFilled(GAMBLER)) {
                findTopNavController().navigate(R.id.action_signInFragment_to_tabsFragment)
            } else {
                findTopNavController().navigate(R.id.action_signInFragment_to_profileFragment)
            }
        }*//*

            //loadAppBarPhoto()

    }*/
}