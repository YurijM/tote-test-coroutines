package com.example.tote_test.ui.tabs.games.game

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentGameBinding
import com.example.tote_test.utils.padLeftZero
import com.example.tote_test.utils.showToast
import java.util.*

class GameFragment : Fragment(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: FragmentGameBinding

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

        binding.gameStart.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()

            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)

            datePickerDialog.show()
        }

        val data = arrayOf("Java", "Python", "C++", "C#", "Angular", "Go")

        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner, data)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)

        val spinner = binding.gameListGroups
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                showToast(parent.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        return binding.root
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

        binding.gameStart.text = start
    }
}