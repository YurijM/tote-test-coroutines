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
        var addCoefficient: Double = 1.0,
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
        val sumStakes = gamblers.sumOf { item -> item.stake }.toDouble()
        val averageStake = sumStakes / gamblersCount

        var secondPartWinning = sumStakes

        winnerGamblers.forEach { gambler ->
            val firstPartCoefficient = when (gambler.place) {
                1 -> 3
                2 -> 6
                else -> 12
            }

            secondPartWinning -= (gamblersCount / firstPartCoefficient) * gambler.stake
        }

        val winnerAttrs = getWinnerAttrs(winnerGamblers)

        var attr = winnerAttrs.find { item -> item.place == 1 }
        if (attr != null) attr.addCoefficient = calcAddCoefficient(attr, winnerAttrs)

        attr = winnerAttrs.find { item -> item.place == 2 }
        if (attr != null) attr.addCoefficient = calcAddCoefficient(attr, winnerAttrs)

        var sumCoefficients = 0.0

        winnerGamblers.forEach { gambler ->
            sumCoefficients += (gambler.stake / averageStake) *
                    (winnerAttrs.find { item -> item.place == gambler.place }?.addCoefficient ?: 1.0)
        }

        val sumForCalc = secondPartWinning / sumCoefficients

        winnerGamblers.forEach { gambler ->
            val firstPartCoefficient = when (gambler.place) {
                1 -> 3
                2 -> 6
                else -> 12
            }

            val firstPartWinning = (gamblersCount / firstPartCoefficient) * gambler.stake

            val stakeCoefficient = gambler.stake / averageStake

            val coefficient = stakeCoefficient * (winnerAttrs.find { item -> item.place == gambler.place }?.addCoefficient ?: 1.0)

            winners.add(
                WinnerModel(
                    id = gambler.id,
                    photoUrl = gambler.photoUrl,
                    place = gambler.place,
                    stake = gambler.stake,
                    winning = firstPartWinning + coefficient * sumForCalc,
                )
            )
        }

        adapterWinners.setWinners(winners)
    }

    private fun calcAddCoefficient(attr: WinnerAttr, attrs: List<WinnerAttr>): Double =
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