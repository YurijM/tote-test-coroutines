package com.example.tote_test.ui.tabs.games.game

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentGameBinding
import com.example.tote_test.models.GameModel
import com.example.tote_test.ui.main.MainViewModel
import com.example.tote_test.utils.*
import java.util.*

class GameFragment : Fragment(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: FragmentGameBinding
    private val viewModel by viewModels<GameViewModel>()
    private val viewModelGames: MainViewModel by viewModels()
    private val viewModelTeams: MainViewModel by viewModels()

    private lateinit var game: GameModel
    private var newDay = ""
    private var newMonth = ""
    private var newYear = ""
    private var newHour = ""
    private var newMinute = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(layoutInflater, container, false)

        game = GameFragmentArgs.fromBundle(requireArguments()).game ?: GameModel()
        toLog("game: $game")

        initFields()

        observeStatus()

        return binding.root
    }

    private fun initFields() {
        initStartDate()
        initGroups()
        initTeams()
        initGameNumber()
        initSave()
    }

    private fun initSave() {
        binding.gameSave.setOnClickListener {
            with(binding) {
                game.id = gameInputNumber.text.toString().toInt()
                game.start = convertDateTimeToTimestamp(gameStartDate.text.toString())
                //game.group = gameListGroups.selectedItem.toString()
                game.group = GROUPS.find { it.number == gameListGroups.selectedItemPosition + 1 }?.group ?: GROUPS[0].group
                game.team1 = gameListTeams1.selectedItem.toString()
                game.team2 = gameListTeams2.selectedItem.toString()
            }

            if (game.team1 != game.team2) {
                viewModel.saveGame(game)
            } else {
                showToast(getString(R.string.error_team_selection))
            }
        }
    }

    private fun initGameNumber() {
        with(binding.gameInputNumber) {
            addTextChangedListener {
                binding.gameSave.isEnabled = !it.isNullOrBlank()
            }

            if (game.id == 0) {
                setText((viewModelGames.games.value?.size?.plus(1)).toString())
            } else {
                setText(game.id.toString())
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initStartDate() {
        /*val std = SimpleDateFormat("dd.MM.yyyy hh:mm")
        binding.gameStartDate.text = std.format(Date())*/

        binding.gameStartDate.text = if (game.id == 0) {
            Calendar.getInstance().time.time.toString().asTime(toLocale = true)
        } else {
            game.start.asTime()
        }

        binding.gameEditStart.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()

            if (game.id > 0) {
                calendar.time = Date(game.start.toLong())
            }

            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)

            datePickerDialog.show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        newDay = padLeftZero(dayOfMonth)
        newYear = year.toString()
        newMonth = padLeftZero(month + 1)

        val calendar: Calendar = Calendar.getInstance()

        if (game.id > 0) {
            calendar.time = Date(game.start.toLong())
        }

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), this, hour, minute, true)

        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        newHour = padLeftZero(hourOfDay)
        newMinute = padLeftZero(minute)

        val start = "$newDay.$newMonth.$newYear $newHour:$newMinute"

        binding.gameStartDate.text = start
    }

    private fun initGroups() {
        val groups = GROUPS.map {
            if (it.group.length == 1) {
                resources.getString(R.string.group, it.group)
            } else {
                it.group
            }
        }

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            groups
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val group = groups.find { item -> item.contains(game.group) }

        initSpinner(binding.gameListGroups, adapter, group ?: groups[0])
    }

    private fun initTeams() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            viewModelTeams.teams.value?.sortedBy { it.team }?.map { it.team } ?: emptyList<String>()
        )

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)

        initSpinner(binding.gameListTeams1, adapter, game.team1)
        initSpinner(binding.gameListTeams2, adapter, game.team2)
    }

    private fun initSpinner(spinner: Spinner, adapter: ArrayAdapter<String>, defaultValue: String) {
        spinner.adapter = adapter

        adapter.getPosition(defaultValue)
        spinner.setSelection(adapter.getPosition(defaultValue))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //showToast(parent.getItemAtPosition(position).toString())
                //showToast(GROUPS[position].group)

                //binding.gameTeams.text = spinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun observeStatus() = viewModel.status.observe(viewLifecycleOwner) {
        when (it) {
            is Resource.Loading -> {
                binding.gameProgressBar.isVisible = true
            }
            is Resource.Success -> {
                binding.gameProgressBar.isInvisible = true
                findTopNavController().navigate(R.id.action_gameFragment_to_gamesFragment)
            }
            is Resource.Error -> {
                binding.gameProgressBar.isInvisible = true
                fixError(it.message.toString())
            }
        }
    }
}