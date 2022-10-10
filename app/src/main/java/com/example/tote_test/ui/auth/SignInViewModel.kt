package com.example.tote_test.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.utils.*
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    private val _status = MutableLiveData<Resource<AuthResult>>()
    val status: LiveData<Resource<AuthResult>> = _status

    fun signIn() {
        if (!AppPreferences.getIsAuth()) {
            _status.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.IO) {
                val result = REPOSITORY.signIn()
                _status.postValue(result)
            }
        }
    }
}
