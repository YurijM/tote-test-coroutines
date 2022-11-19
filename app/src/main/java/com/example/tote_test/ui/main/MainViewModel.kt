package com.example.tote_test.ui.main

import androidx.lifecycle.ViewModel
import com.example.tote_test.utils.REPOSITORY

class MainViewModel : ViewModel() {
    val emails = REPOSITORY.emails
    val gambler = REPOSITORY.gambler
    val games = REPOSITORY.games
    val stakes = REPOSITORY.stakes

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