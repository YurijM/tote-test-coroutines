package com.example.tote_test.ui.tabs.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.databinding.FragmentRatingBinding
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.GAMBLER
import com.example.tote_test.utils.toLog

class RatingFragment : Fragment() {
    private lateinit var binding: FragmentRatingBinding
    private lateinit var viewModel: RatingViewModel
    private val adapter = RatingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")
        toLog("Rating -> GAMBLER: $GAMBLER")

        binding = FragmentRatingBinding.inflate(layoutInflater, container, false)

        val recyclerView = binding.ratingList
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this)[RatingViewModel::class.java]

        observeGambler()

        return binding.root
    }

    private fun observeGambler() = viewModel.gamblers.observe(viewLifecycleOwner) {
        val gamblers = it.sortedWith(compareBy(GamblerModel::place, GamblerModel::name))
        adapter.setGamblers(gamblers)
    }

}