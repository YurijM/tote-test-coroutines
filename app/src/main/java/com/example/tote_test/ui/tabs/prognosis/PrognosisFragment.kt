package com.example.tote_test.ui.tabs.prognosis

import android.annotation.SuppressLint
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
import com.example.tote_test.utils.asTime
import java.text.SimpleDateFormat
import java.util.*

class PrognosisFragment : Fragment() {
    private lateinit var binding: FragmentPrognosisBinding
    private val viewModelMain: MainViewModel by viewModels()
    private val viewModel: PrognosisViewModel by viewModels()
    private lateinit var games: List<GameModel>
    private val adapter = PrognosisAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        binding = FragmentPrognosisBinding.inflate(layoutInflater, container, false)

        val recyclerView = binding.prognosisList
        recyclerView.adapter = adapter

        observeGames()

        return binding.root
    }

    private fun observePrognosis() = viewModel.prognosis.observe(viewLifecycleOwner) {
        val stakes = arrayListOf<StakeModel>()

        it.forEach {
            viewModelMain.stakesAll.value?.filter { item -> item.gameId == it.gameId }?.forEach { stake ->
                val gambler = viewModel.gamblers.value?.find { gambler -> gambler.id == stake.gamblerId }
                if (gambler != null) {
                    stake.gamblerId = gambler.nickname
                }
                stakes.add(stake)
            }
        }

        adapter.setPrognosis(it.sortedByDescending { prognosis -> prognosis.gameId })
        adapter.setStakes(stakes.sortedBy { stake -> stake.gamblerId })
    }

    @SuppressLint("SimpleDateFormat")
    private fun observeGames() = viewModelMain.games.observe(viewLifecycleOwner) {
        val now = Calendar.getInstance().time.time
        val nowLocale = SimpleDateFormat("dd.MM.yyyy HH:mm").parse(now.toString().asTime(toLocale = true))?.time ?: 0

        games = it.filter { item -> (nowLocale > item.start.toLong()) && item.goal1.isNotBlank() && item.goal2.isNotBlank() }

        //games = it.filter { item -> now > item.start.toLong() && item.goal1.isNotBlank() && item.goal2.isNotBlank() }

        if (games.isNotEmpty()) {
            binding.prognosisTournamentNotStarted.isGone = true

            observePrognosis()
        } else {
            binding.prognosisTournamentNotStarted.text = "Турнир ещё не начался"
            binding.prognosisTournamentNotStarted.isGone = false
        }
    }
}