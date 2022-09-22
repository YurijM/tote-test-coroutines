package com.example.tote_test.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.toLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val _gambler = MutableLiveData<GamblerModel>()
    val gambler: LiveData<GamblerModel> = _gambler

    init {
        toLog("init")
        eventListenerGamblerLiveData()
    }

    private fun eventListenerGamblerLiveData() = viewModelScope.launch(Dispatchers.IO) {
        REPOSITORY.eventListenerGamblerLiveData(_gambler)
    }

    fun signOut() {
        REPOSITORY.signOut()
    }
}