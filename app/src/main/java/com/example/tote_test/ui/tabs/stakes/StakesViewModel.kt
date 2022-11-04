package com.example.tote_test.ui.tabs.stakes

import androidx.lifecycle.ViewModel
import com.example.tote_test.utils.REPOSITORY

class StakesViewModel : ViewModel() {
    val stakes = REPOSITORY.stakes
}