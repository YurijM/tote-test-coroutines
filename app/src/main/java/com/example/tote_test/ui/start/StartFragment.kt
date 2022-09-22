package com.example.tote_test.ui.start

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentStartBinding
import com.example.tote_test.utils.findTopNavController

class StartFragment : Fragment(R.layout.fragment_start) {
    private lateinit var binding: FragmentStartBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentStartBinding.bind(view)

        binding.startSignIn.setOnClickListener {
            findTopNavController().navigate(R.id.action_startFragment_to_signInFragment)
        }

        binding.startSignUp.setOnClickListener {
            findTopNavController().navigate(R.id.action_startFragment_to_signUpFragment)
        }
    }
}