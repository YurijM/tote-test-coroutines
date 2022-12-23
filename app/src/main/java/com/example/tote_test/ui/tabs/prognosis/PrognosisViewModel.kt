package com.example.tote_test.ui.tabs.prognosis

import androidx.lifecycle.ViewModel
import com.example.tote_test.utils.REPOSITORY

class PrognosisViewModel: ViewModel() {
    val prognosis = REPOSITORY.prognosis
    val gamblers = REPOSITORY.gamblers
}