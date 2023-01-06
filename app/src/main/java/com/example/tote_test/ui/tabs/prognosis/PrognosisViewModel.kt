package com.example.tote_test.ui.tabs.prognosis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.Resource

class PrognosisViewModel: ViewModel() {
    private val _status = MutableLiveData<Resource<Boolean>>()
    val status: LiveData<Resource<Boolean>> = _status

    val prognosis = REPOSITORY.prognosis
    val gamblers = REPOSITORY.gamblers
}