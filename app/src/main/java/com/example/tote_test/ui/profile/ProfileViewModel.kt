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

    /*private val _isPhotoUrl = MutableLiveData<Boolean>(false)
    val isPhotoUrl: LiveData<Boolean> = _isPhotoUrl*/

    init {
        //_profile.value = GAMBLER
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


    /*fun saveGamblerToDB(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        showProgress()

        val profile: GamblerModel = _profile.value as GamblerModel

        val dataMap = mutableMapOf<String, Any>()

        dataMap[GAMBLER_NICKNAME] = profile.nickname.trim()
        dataMap[GAMBLER_FAMILY] = profile.family.trim()
        dataMap[GAMBLER_NAME] = profile.name.trim()
        dataMap[GAMBLER_GENDER] = profile.gender
    }*/

    /*fun saveImageToStorage(onSuccess: () -> Unit, onFail: (String) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        toLog("_photoUri.value: ${_photoUri.value}")

        if (_photoUri.value == null) {
           return@launch
        }

        viewModelScope.launch {
            toLog("saveImageToStorage -> showProgress")
            showProgress()
        }
        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_PHOTO).child(CURRENT_ID)

        _photoUri.value?.let {
            REPOSITORY.saveImageToStorage(it, path,
                {
                    viewModelScope.launch {
                        toLog("saveImageToStorage -> hideProgress")
                        hideProgress()
                    }
                    onSuccess()
                },
                { error ->
                    viewModelScope.launch {
                        toLog("saveImageToStorage -> fail: $error")
                        hideProgress()
                    }
                    onFail(error)
                }
            )
        }
    }*/

    /*fun getUrlFromStorage(onSuccess: (String) -> Unit, onFail: (String) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_PHOTO).child(CURRENT_ID)

        viewModelScope.launch {
            toLog("getUrlFromStorage -> showProgress")
            showProgress()
        }

        REPOSITORY.getUrlFromStorage(path,
            {
                viewModelScope.launch {
                    toLog("getUrlFromStorage -> hideProgress")
                    hideProgress()
                }
                onSuccess(it)
            },
            { error ->
                viewModelScope.launch {
                    toLog("getUrlFromStorage -> fail: $error")
                    hideProgress()
                }
                onFail(error)
            }
        )
    }*/

    /*fun savePhotoUrlToDB(url: String, onSuccess: () -> Unit, onFail: (String) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        viewModelScope.launch {
            toLog("savePhotoUrlToDB -> showProgress")
            showProgress()
        }

        REPOSITORY.savePhotoUrlToDB(url,
            {
                viewModelScope.launch {
                    toLog("savePhotoUrlToDB -> hideProgress")
                    hideProgress()
                }
                onSuccess()
            },
            { error ->
                viewModelScope.launch {
                    toLog("savePhotoUrlToDB -> fail: $error")
                    hideProgress()
                }
            }
        )
    }*/


    /*fun saveImageToStorage(onSuccess: (url: String) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        toLog("_photoUri.value: ${_photoUri.value}")

        if (_photoUri.value == null) {
            return@launch
        }

        showProgress()

        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_PHOTO).child(CURRENT_ID)

        _photoUri.value?.let { it ->
            viewModelScope.launch(Dispatchers.IO) {
                *//*REPOSITORY.saveImageToStorage(it, path) {
                    viewModelScope.launch(Dispatchers.IO) {
                        REPOSITORY.getUrlFromStorage(path) { url ->
                            viewModelScope.launch(Dispatchers.IO) {
                                REPOSITORY.savePhotoUrlToDB(url) {
                                    hideProgress()
                                    onSuccess(url)
                                }
                            }
                        }
                    }
                }*//*
            }
        }
    }*/

    /*private fun showProgress() {
        _inProgress.postValue(true)
    }*/

    /*private fun hideProgress() {
        _inProgress.postValue(false)
    }*/
}
