package com.example.tote_test.ui.auth

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.R
import com.example.tote_test.utils.*
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel() : ViewModel() {
    private val _status = MutableLiveData<Resource<AuthResult>>()
    val status: LiveData<Resource<AuthResult>> = _status

    fun signUp() {
        val error = if (!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()) {
                APP_ACTIVITY.resources.getString(R.string.error_valid_email)
            } else null

        error?.let {
            _status.postValue(Resource.Error(it))
            return
        }
        _status.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val result = REPOSITORY.signUp()
            toLog("SignUpViewModel -> signUp -> result: $result")
            _status.postValue(result)
        }
    }
}
