package com.example.tote_test.ui.tabs.prognosis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrognosisViewModel: ViewModel() {
    private val _status = MutableLiveData<Resource<Boolean>>()
    val status: LiveData<Resource<Boolean>> = _status

    val prognosis = REPOSITORY.prognosis
    val gamblers = REPOSITORY.gamblers

    fun saveGambler(gambler: GamblerModel) {
        _status.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val result = REPOSITORY.saveGambler(gambler.id, gambler).data ?: false

            _status.postValue(Resource.Success(result))
        }
    }
}