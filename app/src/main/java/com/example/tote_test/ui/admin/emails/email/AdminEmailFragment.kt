package com.example.tote_test.ui.admin.emails.email

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tote_test.databinding.FragmentAdminEmailBinding

class AdminEmailFragment : Fragment() {
    private lateinit var binding: FragmentAdminEmailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminEmailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}