package com.example.tote_test.ui.tabs.prognosis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tote_test.databinding.FragmentPrognosisBinding
import com.example.tote_test.models.GameModel
import com.example.tote_test.models.StakeModel
import com.example.tote_test.ui.main.MainViewModel
import com.example.tote_test.utils.CURRENT_ID
import java.util.*

class PrognosisFragment : Fragment() {
    private lateinit var binding: FragmentPrognosisBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var stakes: List<StakeModel>
    private lateinit var games: List<GameModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        binding = FragmentPrognosisBinding.inflate(layoutInflater, container, false)

        observeGames()

        return binding.root
    }

    private fun observeStakes() = viewModel.stakes.observe(viewLifecycleOwner) {
        stakes = it.filter { item -> item.gamblerId == CURRENT_ID }

        //observeGames()
    }

    private fun observeGames() = viewModel.games.observe(viewLifecycleOwner) {
        val now = Calendar.getInstance().time.time

        games = it.filter { item -> now < item.start.toLong() }

        if (games.isNotEmpty()) {

            binding.prognosisTournamentNotStarted.text = games[0].start
            binding.prognosisTournamentNotStarted.isGone = false

            binding.prognosisCoefficients.isGone = true
        }

        //observeGames()
    }
}