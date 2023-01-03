package com.example.tote_test.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.models.PrognosisModel
import com.example.tote_test.models.StakeModel
import com.example.tote_test.utils.REPOSITORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val emails = REPOSITORY.emails
    val gambler = REPOSITORY.gambler
    val games = REPOSITORY.games
    val stakes = REPOSITORY.stakes
    val stakesAll = REPOSITORY.stakesAll
    val teams = REPOSITORY.teams
    val gamblers = REPOSITORY.gamblers

    fun saveGambler(gambler: GamblerModel) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.saveGambler(gambler.id, gambler).data
        }
    }

    fun saveStake(stake: StakeModel) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.saveStake(stake)
        }
    }

    fun savePrognosis(gameId: String, prognosis: PrognosisModel) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.savePrognosis(gameId, prognosis).data
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