package com.example.tote_test.ui.admin.group_games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.GameModel
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminGroupGamesViewModel : ViewModel() {
    private val _status = MutableLiveData<Resource<Boolean>>()
    val status: LiveData<Resource<Boolean>> = _status

    val games = REPOSITORY.games

    fun saveGame(game: GameModel) {
        _status.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val result = REPOSITORY.saveGame(game.id.toString(), game).data == true

            _status.postValue(Resource.Success(result))
        }
    }
}