package com.example.tote_test.ui.tabs.games.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.GameModel
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel: ViewModel() {
    private val _status = MutableLiveData<Resource<Boolean>>()
    val status: LiveData<Resource<Boolean>> = _status

    private val _gameNumber = MutableLiveData("")

    fun changeGameNumber(gameNumber: String) {
        _gameNumber.value = gameNumber
    }

    fun saveGame(game: GameModel) {
        _status.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val result = REPOSITORY.saveGame(game.id.toString(), game).data == true

            _status.postValue(Resource.Success(result))
        }
    }}