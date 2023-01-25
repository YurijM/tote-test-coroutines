package com.example.tote_test.ui.admin.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.GameModel
import com.example.tote_test.models.StakeModel
import com.example.tote_test.models.TeamModel
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminSettingsViewModel : ViewModel() {
    private val _status = MutableLiveData<Resource<Boolean>>()
    val status: LiveData<Resource<Boolean>> = _status

    fun addGame(game: GameModel) {
        _status.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val result = REPOSITORY.addGame(game)
            _status.postValue(result)
        }
    }

    fun saveStake(stake: StakeModel) {
        _status.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val result = REPOSITORY.saveStake(stake)
            _status.postValue(result)
        }
    }

    /*fun saveStakes(stakes: List<StakeModel>) {
        _status.postValue(Resource.Loading())

        lateinit var result: Resource<Boolean>

        result = Resource.Success(true)

        stakes.forEach { stake ->
            viewModelScope.launch(Dispatchers.IO) {
                result = REPOSITORY.saveStake(stake)
            }
        }

        _status.postValue(result)
    }*/

    fun addTeam(team: TeamModel) {
        _status.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val result = REPOSITORY.addTeam(team)
            _status.postValue(result)
        }
    }
}