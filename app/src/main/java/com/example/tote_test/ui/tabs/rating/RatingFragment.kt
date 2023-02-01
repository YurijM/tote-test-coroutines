package com.example.tote_test.ui.tabs.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tote_test.databinding.FragmentRatingBinding
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.models.WinnerModel
import com.example.tote_test.utils.GAMBLER
import com.example.tote_test.utils.findTopNavController

class RatingFragment : Fragment() {
    private lateinit var binding: FragmentRatingBinding
    private val viewModel: RatingViewModel by viewModels()
    private val adapterRating = RatingAdapter { gambler -> onListItemClick(gambler) }
    private val adapterWinners = RatingWinnersAdapter()

    private data class WinnerAttr(
        val place: Int,
        val points: Double = 0.0,
        val gamblersCount: Int = 0,
        var addCoef: Double = 1.0,
    )

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

        val winners = arrayListOf<WinnerModel>()

        val winnerGamblers = gamblers
            .filter { item -> (item.place in 1..3) }
            .sortedBy { item -> item.place }

        val gamblersCount = it.size.toDouble()

        val averageStake = gamblers.sumOf { item -> item.stake } / gamblersCount

        val winnerAttrs = getWinnerAttrs(winnerGamblers)

        var attr = winnerAttrs.find { item -> item.place == 1 }
        if (attr != null) attr.addCoef = calcAddCoef(attr, winnerAttrs)

        attr = winnerAttrs.find { item -> item.place == 2 }
        if (attr != null) attr.addCoef = calcAddCoef(attr, winnerAttrs)

        winnerGamblers.forEach { gambler ->
            val firstPartCoef = when (gambler.place) {
                1 -> 3
                2 -> 6
                else -> 12
            }

            val firstPartWinning = (gamblersCount / firstPartCoef) * gambler.stake

            val stakeCoef = gambler.stake / averageStake

            val coef = stakeCoef * (winnerAttrs.find { item -> item.place == gambler.place }?.addCoef ?: 1.0)

            winners.add(
                WinnerModel(
                    id = gambler.id,
                    nickname = gambler.nickname,
                    photoUrl = gambler.photoUrl,
                    place = gambler.place,
                    winning = firstPartWinning,
                )
            )
        }

        adapterWinners.setWinners(winners)
    }

    private fun calcAddCoef(attr: WinnerAttr, attrs: List<WinnerAttr>): Double =
        if (attr.place == 1) {
            if (attr.gamblersCount <= 2) {
                (attr.points - (attrs.find { item -> item.place == 3 }?.points
                    ?: attrs.find { item -> item.place == 2 }?.points ?: 0.0)) / 10 + 1
            } else 1.0
        } else if (attr.place == 2) {
            if (attr.gamblersCount == 1) {
                (attr.points - (attrs.find { item -> item.place == 3 }?.points ?: 0.0)) / 10 + 1
            } else 1.0
        } else 1.0

    private fun getWinnerAttrs(winnerGamblers: List<GamblerModel>): List<WinnerAttr> =
        arrayListOf(
            WinnerAttr(
                place = 1,
                points = winnerGamblers.find { item -> item.place == 1 }?.points ?: 0.0,
                gamblersCount = winnerGamblers.filter { item -> item.place == 1 }.size,
            ),
            WinnerAttr(
                place = 2,
                points = winnerGamblers.find { item -> item.place == 2 }?.points ?: 0.0,
                gamblersCount = winnerGamblers.filter { item -> item.place == 2 }.size,
            ),
            WinnerAttr(
                place = 3,
                points = winnerGamblers.find { item -> item.place == 3 }?.points ?: 0.0,
                gamblersCount = winnerGamblers.filter { item -> item.place == 3 }.size,
            ),
        )
}