package com.example.tote_test.ui.tabs.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.databinding.FragmentGamesBinding
import com.example.tote_test.models.GameModel
import com.example.tote_test.ui.auth.SignUpViewModel
import com.example.tote_test.utils.convertDateTimeToTimestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GamesFragment : Fragment() {
    private lateinit var binding: FragmentGamesBinding
    private lateinit var viewModel: GamesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]

        binding = FragmentGamesBinding.inflate(layoutInflater, container, false)

        val games = ArrayList<GameModel>()
        games.add(
            GameModel(
                id = 2,
                group = "B",
                team1 = "Англия",
                team2 = "Иран",
                start = convertDateTimeToTimestamp("21.11.2022 16:00").toString()
            )
        )

        binding.addGame.setOnClickListener {
            viewModel.addGame(games[0])
        }

        return binding.root
    }

    companion object {

    }
}