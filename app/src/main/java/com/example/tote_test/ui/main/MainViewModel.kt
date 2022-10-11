package com.example.tote_test.ui.main

import androidx.lifecycle.ViewModel
import com.example.tote_test.utils.REPOSITORY

class MainViewModel : ViewModel() {
    val gambler = REPOSITORY.gambler

    /*private val _gambler = MutableLiveData<GamblerModel>()
    val gambler: LiveData<GamblerModel> = _gambler

    init {
        eventListenerGamblerLiveData()
    }

    private fun eventListenerGamblerLiveData() = viewModelScope.launch(Dispatchers.IO) {
        REPOSITORY.eventListenerGamblerLiveData(_gambler)
    }*/

    fun signOut() {
        REPOSITORY.signOut()
    }
}