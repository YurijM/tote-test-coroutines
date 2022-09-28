package com.example.tote_test.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.CURRENT_ID
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.Resource
import com.example.tote_test.utils.isProfileFilled
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _status = MutableLiveData<Resource<Boolean>>()
    val status: LiveData<Resource<Boolean>> = _status

    private val _profile = MutableLiveData<GamblerModel>()
    val profile: LiveData<GamblerModel> = _profile

    private val _photoUri = MutableLiveData<Uri>()
    val photoUri: LiveData<Uri> = _photoUri

    private val _isNickname = MutableLiveData(false)
    private val _isFamily = MutableLiveData(false)
    private val _isName = MutableLiveData(false)
    private val _isGender = MutableLiveData(false)

    private val _isFieldsFilled = MutableLiveData(false)
    val isFieldsFilled: LiveData<Boolean> = _isFieldsFilled

    init {
        eventListenerGamblerLiveData()
    }

    override fun onCleared() {
        removeListenerGamblerLiveData()
    }

    fun changeNickname(nickname: String) {
        _profile.value?.nickname = nickname
        _isNickname.value = nickname.isNotBlank()
        _isFieldsFilled.value = checkFieldsFilled()
    }

    fun changeFamily(family: String) {
        _profile.value?.family = family
        _isFamily.value = family.isNotBlank()
        _isFieldsFilled.value = checkFieldsFilled()
    }

    fun changeName(name: String) {
        _profile.value?.name = name
        _isName.value = name.isNotBlank()
        _isFieldsFilled.value = checkFieldsFilled()
    }

    fun changeGender(gender: String) {
        _profile.value?.gender = gender
        _isGender.value = gender.isNotBlank()
        _isFieldsFilled.value = checkFieldsFilled()
    }

    fun changePhotoUrl(uri: Uri) {
        _photoUri.value = uri
    }

    private fun checkFieldsFilled(): Boolean {
        return (_isNickname.value == true)
                && (_isFamily.value == true)
                && (_isName.value == true)
                && (_isGender.value == true)
    }

    fun checkProfileFilled(): Boolean {
        return if (_profile.value != null) {
            isProfileFilled(_profile.value!!)
        } else {
            false
        }
    }

    private fun eventListenerGamblerLiveData() = viewModelScope.launch(Dispatchers.IO) {
        REPOSITORY.eventListenerGamblerLiveData(_profile)
    }

    private fun removeListenerGamblerLiveData() = viewModelScope.launch(Dispatchers.IO) {
        REPOSITORY.removeEventListener(REPOSITORY.listenerGambler)
    }

    fun saveProfile(gambler: GamblerModel) {
        _status.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            var result = REPOSITORY.saveGambler(CURRENT_ID, gambler).data ?: false

            if (result && (_photoUri.value != null)) {
                result = REPOSITORY.saveGamblerPhoto(CURRENT_ID, _photoUri.value!!).data ?: false
            }

            _status.postValue(Resource.Success(result))
        }
    }
}
