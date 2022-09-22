package com.example.tote_test.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _inProgress = MutableLiveData(false)
    val inProgress: LiveData<Boolean> = _inProgress

    private val _profile = MutableLiveData<GamblerModel>()
    val profile: LiveData<GamblerModel> = _profile

    private val _photoUri = MutableLiveData<Uri>()
    val photoUri: LiveData<Uri> = _photoUri

    init {
        _profile.value = GAMBLER

        //getGamblerLiveData()
        /*getGambler {
            _profile.value = GAMBLER
            toLog("ProfileViewModel -> init -> GAMBLER: $GAMBLER")
        }*/
    }

    fun changeNickname(nickname: String) {
        _profile.value?.nickname = nickname
    }

    fun changeFamily(family: String) {
        _profile.value?.family = family
    }

    fun changeName(name: String) {
        _profile.value?.name = name
    }

    fun changeGender(gender: String) {
        _profile.value?.gender = gender
    }

    fun changePhotoUrl(uri: Uri) {
        _photoUri.value = uri
    }

    fun checkProfileFilled(): Boolean {
        return if (_profile.value != null) {
            isProfileFilled(_profile.value!!)
        } else {
            false
        }
    }

    /*private fun getGamblerLiveData() = viewModelScope.launch(Dispatchers.IO) {
        REPOSITORY.getGamblerLiveData(_profile)
    }*/

    fun saveGamblerToDB(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        showProgress()

        val profile: GamblerModel = _profile.value as GamblerModel

        val dataMap = mutableMapOf<String, Any>()

        dataMap[GAMBLER_NICKNAME] = profile.nickname.trim()
        dataMap[GAMBLER_FAMILY] = profile.family.trim()
        dataMap[GAMBLER_NAME] = profile.name.trim()
        dataMap[GAMBLER_GENDER] = profile.gender

        /*REPOSITORY.saveGamblerToDB(dataMap) {
            hideProgress()

            onSuccess()
        }*/
    }

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


    fun saveImageToStorage(onSuccess: (url: String) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        toLog("_photoUri.value: ${_photoUri.value}")

        if (_photoUri.value == null) {
            return@launch
        }

        showProgress()

        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_PHOTO).child(CURRENT_ID)

        _photoUri.value?.let { it ->
            viewModelScope.launch(Dispatchers.IO) {
                /*REPOSITORY.saveImageToStorage(it, path) {
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
                }*/
            }
        }
    }

    private fun showProgress() {
        _inProgress.postValue(true)
    }

    private fun hideProgress() {
        _inProgress.postValue(false)
    }
}
