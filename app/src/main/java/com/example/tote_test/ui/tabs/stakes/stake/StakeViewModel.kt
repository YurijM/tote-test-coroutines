package com.example.tote_test.ui.tabs.stakes.stake

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.StakeModel
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StakeViewModel : ViewModel() {
    private val _status = MutableLiveData<Resource<Boolean>>()
    val status: LiveData<Resource<Boolean>> = _status

    private val _goal1 = MutableLiveData("")
    private val _goal2 = MutableLiveData("")
    private val _addGoal1 = MutableLiveData("")
    private val _addGoal2 = MutableLiveData("")
    private val _penalty = MutableLiveData("")

    fun changeGoal1(goal: String) {
        _goal1.value = goal
    }

    fun changeGoal2(goal: String) {
        _goal2.value = goal
    }

    fun changeAddGoal1(goal: String) {
        _addGoal1.value = goal
    }

    fun changeAddGoal2(goal: String) {
        _addGoal2.value = goal
    }

    fun changePenalty(team: String) {
        _penalty.value = team
    }

    fun saveStake(stake: StakeModel) {
        _status.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val result = REPOSITORY.saveStake(stake.gameId.toString(), stake).data == true

            _status.postValue(Resource.Success(result))
        }
    }
}