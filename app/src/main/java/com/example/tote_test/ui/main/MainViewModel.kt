package com.example.tote_test.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.REPOSITORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val emails = REPOSITORY.emails
    val gambler = REPOSITORY.gambler
    val games = REPOSITORY.games
    val stakes = REPOSITORY.stakes
    val teams = REPOSITORY.teams
    val gamblers = REPOSITORY.gamblers

    fun saveGambler(gambler: GamblerModel) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.saveGambler(gambler.id, gambler).data
        }
    }

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