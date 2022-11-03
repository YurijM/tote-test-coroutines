package com.example.tote_test.ui.tabs.stakes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tote_test.databinding.FragmentStakesBinding

class StakesFragment : Fragment() {
    private lateinit var binding: FragmentStakesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        binding = FragmentStakesBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}