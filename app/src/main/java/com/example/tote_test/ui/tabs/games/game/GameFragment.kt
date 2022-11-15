package com.example.tote_test.ui.tabs.games.game

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentGameBinding
import com.example.tote_test.models.GameModel
import com.example.tote_test.ui.main.MainViewModel
import com.example.tote_test.utils.GROUPS
import com.example.tote_test.utils.TEAMS
import com.example.tote_test.utils.padLeftZero
import java.text.SimpleDateFormat
import java.util.*

class GameFragment : Fragment(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: FragmentGameBinding
    private val viewModel by viewModels<GameViewModel>()
    private val viewModelGames: MainViewModel by viewModels()

    private lateinit var game: GameModel
    private lateinit var inputGameNumber: TextView

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

        initFields()

        return binding.root
    }

    private fun initFields() {
        initStartDate()
        initGroups()
        initTeams()
        initGameNumber()
    }

    private fun initGameNumber() {
        inputGameNumber = binding.gameInputNumber
        viewModel.changeGameNumber((viewModelGames.games.value?.size?.plus(1)).toString())

        inputGameNumber.addTextChangedListener {
            binding.gameSave.isEnabled = !it.isNullOrBlank()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initStartDate() {
        val std = SimpleDateFormat("dd.MM.yyyy hh:mm")
        binding.gameStartDate.text = std.format(Date())

        binding.gameEditStart.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()

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

        val hour = calendar.get(Calendar.HOUR)
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

        initSpinner(binding.gameListGroups, adapter)
    }

    private fun initTeams() {
        val teams = TEAMS.sortedBy { it.team }.map { it.team }

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            teams
        )

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)

        initSpinner(binding.gameListTeams1, adapter)
        initSpinner(binding.gameListTeams2, adapter)
    }

    private fun initSpinner(spinner: Spinner, adapter: ArrayAdapter<String>) {
        spinner.adapter = adapter

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
}