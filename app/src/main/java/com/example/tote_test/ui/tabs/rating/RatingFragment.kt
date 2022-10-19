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
import com.example.tote_test.utils.findTopNavController
import com.example.tote_test.utils.toLog

class RatingFragment : Fragment() {
    private lateinit var binding: FragmentRatingBinding
    private lateinit var viewModel: RatingViewModel
    private val adapter = RatingAdapter { gambler -> onListItemClick(gambler) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object {}.javaClass.enclosingMethod?.name}")

        binding = FragmentRatingBinding.inflate(layoutInflater, container, false)

        val recyclerView = binding.ratingList
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this)[RatingViewModel::class.java]

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
        toLog("Rating -> observeGambler: $GAMBLER")
        val gamblers = it
            .filter { item -> if (GAMBLER.admin) true else item.active }
            .sortedWith(
                compareBy<GamblerModel> { item -> !item.active }
                    .thenBy { item -> item.place }
                    .thenBy { item -> item.nickname }
            )

        adapter.setGamblers(gamblers)
    }
}