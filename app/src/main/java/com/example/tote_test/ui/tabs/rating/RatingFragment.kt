package com.example.tote_test.ui.tabs.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tote_test.databinding.FragmentRatingBinding
import com.example.tote_test.utils.toLog

class RatingFragment : Fragment() {
    private lateinit var binding: FragmentRatingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        binding = FragmentRatingBinding.inflate(layoutInflater, container, false)

        binding.textTest.text = "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n" +
                "Путь. Скорость. Время.\n"

        return binding.root
    }
}