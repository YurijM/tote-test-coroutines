package com.example.tote_test.ui.admin.group_games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.Resource

class AdminGroupGamesViewModel : ViewModel() {
    private val _status = MutableLiveData<Resource<Boolean>>()
    val status: LiveData<Resource<Boolean>> = _status

    val games = REPOSITORY.games
}