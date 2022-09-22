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
                val loginResult = REPOSITORY.signIn()
                _status.postValue(loginResult)
            }
        }
    }

    /*fun getGambler() {
        viewModelScope.launch(Dispatchers.IO) {
            GAMBLER = REPOSITORY.getGambler()
        }
    }*/

    /*fun auth(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        toLog("auth -> CURRENT_ID before: $CURRENT_ID")
        REPOSITORY.signIn {
            toLog("auth -> CURRENT_ID after: $CURRENT_ID")
            if (CURRENT_ID.isNotBlank()) {
                this@SignInViewModel.getGambler {
                    onSuccess()
                }
            }
        }
    }*/
}
