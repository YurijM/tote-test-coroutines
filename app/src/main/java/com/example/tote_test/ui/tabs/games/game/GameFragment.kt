package com.example.tote_test.ui.tabs.games.game

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.example.tote_test.databinding.FragmentGameBinding
import java.util.*

class GameFragment : Fragment(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: FragmentGameBinding

    private var newDay = 0
    private var newMonth = 0
    private var newYear = 0
    private var newHour = 0
    private var newMinute = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(layoutInflater, container, false)

        binding.gameStart.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()

            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)

            datePickerDialog.show()
        }

        return binding.root
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        newDay = dayOfMonth
        newYear = year
        newMonth = month + 1

        val calendar: Calendar = Calendar.getInstance()

        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), this, hour, minute, true)

        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        newHour = hourOfDay
        newMinute = minute

        val start = "$newDay.$newMonth.$newYear $newHour:$newMinute"

        binding.gameStart.text = start
    }
}