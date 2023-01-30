package com.example.tote_test.ui.tabs.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tote_test.databinding.FragmentRatingBinding
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.GAMBLER
import com.example.tote_test.utils.findTopNavController

class RatingFragment : Fragment() {
    private lateinit var binding: FragmentRatingBinding
    private val viewModel: RatingViewModel by viewModels()
    private val adapterRating = RatingAdapter { gambler -> onListItemClick(gambler) }
    private val adapterWinners = RatingWinnersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object {}.javaClass.enclosingMethod?.name}")

        binding = FragmentRatingBinding.inflate(layoutInflater, container, false)

        val recyclerViewRating = binding.ratingList
        recyclerViewRating.adapter = adapterRating

        val recyclerViewWinners = binding.ratingWinners
        recyclerViewWinners.adapter = adapterWinners

        observeGamblers()

        return binding.root
    }

    private fun onListItemClick(gambler: GamblerModel) {
        /*val bundle = Bundle()
        bundle.putSerializable("gambler", gambler)
        findTopNavController().navigate(R.id.action_ratingFragment_to_adminGamblerFragment, bundle)*/

        val action = if (GAMBLER.admin) {
            RatingFragmentDirections.actionRatingFragmentToAdminGamblerFragment(gambler)
        } else {
            RatingFragmentDirections.actionRatingFragmentToAdminGamblerPhotoFragment(gambler.photoUrl, true)
        }
        findTopNavController().navigate(action)
    }

    private fun observeGamblers() = viewModel.gamblers.observe(viewLifecycleOwner) {
        val gamblers = it
            .filter { item -> if (GAMBLER.admin) true else item.active }
            .sortedWith(
                compareBy<GamblerModel> { item -> !item.active }
                    .thenBy { item -> item.place }
                    .thenBy { item -> item.nickname }
            )

        adapterRating.setGamblers(gamblers)

        val winners = it
            .filter { item -> (item.place in 1..3) }
            .sortedBy { item -> item.place }

        adapterWinners.setWinners(winners)
    }
}