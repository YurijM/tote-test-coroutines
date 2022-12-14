package com.example.tote_test.firebase

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tote_test.models.*
import com.example.tote_test.utils.*
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AppValueEventListener(val onSuccess: (DataSnapshot) -> Unit) : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        onSuccess(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {
    }
}

class FirebaseRepository {
    lateinit var listenerGambler: ValueEventListener
    val teams: LiveData<List<TeamModel>> = TeamsLiveData()
    val games: LiveData<List<GameModel>> = GamesLiveData()
    val gambler: LiveData<GamblerModel> = GamblerLiveData()
    val gamblers: LiveData<List<GamblerModel>> = GamblersLiveData()
    val stakes: LiveData<List<StakeModel>> = StakesForCurrentGamblerLiveData()
    val stakesAll: LiveData<List<StakeModel>> = StakesAllLiveData()
    val prognosis: LiveData<List<PrognosisModel>> = PrognosisLiveData()
    val emails: LiveData<List<EmailModel>> = EmailsLiveData()

    init {
        AUTH = FirebaseAuth.getInstance()
    }

    fun initDB() {
        REF_DB_ROOT = FirebaseDatabase.getInstance().reference
        REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference

        CURRENT_ID = AUTH.currentUser?.uid.toString()
    }

    suspend fun signUp(): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = AUTH.createUserWithEmailAndPassword(EMAIL, PASSWORD).await()

                CURRENT_ID = result.user?.uid.toString()
                if (CURRENT_ID.isNotBlank()) {
                    GAMBLER.id = CURRENT_ID
                    GAMBLER.email = EMAIL

                    REF_DB_ROOT.child(NODE_GAMBLERS).child(CURRENT_ID).setValue(GAMBLER).await()
                }
                Resource.Success(result)
            }
        }
    }

    suspend fun addGame(game: GameModel): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeCall {
                REF_DB_ROOT.child(NODE_GAMES).child(game.id.toString()).setValue(game).await()
                Resource.Success(true)
            }
        }
    }

    suspend fun addTeam(team: TeamModel): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeCall {
                REF_DB_ROOT.child(NODE_TEAMS).child(team.team).setValue(team).await()
                Resource.Success(true)
            }
        }
    }

    suspend fun signIn(): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = AUTH.signInWithEmailAndPassword(EMAIL, PASSWORD).await()
                CURRENT_ID = AUTH.currentUser?.uid.toString()
                Resource.Success(result)
            }
        }
    }

    fun signOut() {
        AppPreferences.setIsAuth(false)
        AUTH.signOut()
    }

    suspend fun getGambler(id: String): GamblerModel {
        var gambler = GamblerModel()

        try {
            gambler = REF_DB_ROOT.child(NODE_GAMBLERS).child(id)
                .get()
                .await()
                .getValue(GamblerModel::class.java) ?: GamblerModel()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                fixError(e.toString())
            }
        }

        return gambler
    }

    suspend fun saveEmail(email: EmailModel): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeCall {
                REF_DB_ROOT.child(NODE_EMAILS).child(email.id.toString()).setValue(email).await()
                Resource.Success(true)
            }
        }
    }

    suspend fun removeEmail(id: String): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeCall {
                REF_DB_ROOT.child(NODE_EMAILS).child(id).removeValue().await()
                Resource.Success(true)
            }
        }
    }

    suspend fun saveGame(id: String, game: GameModel): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeCall {
                REF_DB_ROOT.child(NODE_GAMES).child(id).setValue(game).await()
                Resource.Success(true)
            }
        }
    }

    suspend fun saveStake(stake: StakeModel): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeCall {
                //REF_DB_ROOT.child(NODE_STAKES).child(stake.gameId.toString()).child(stake.gamblerId).setValue(stake).await()
                REF_DB_ROOT.child(NODE_STAKES).child("${stake.gameId}-${stake.gamblerId}").setValue(stake).await()
                Resource.Success(true)
            }
        }
    }

    suspend fun savePrognosis(gameId: String, prognosis: PrognosisModel): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeCall {
                REF_DB_ROOT.child(NODE_PROGNOSIS).child(gameId).setValue(prognosis).await()
                Resource.Success(true)
            }
        }
    }

    suspend fun saveGambler(id: String, gambler: GamblerModel): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeCall {
                REF_DB_ROOT.child(NODE_GAMBLERS).child(id).setValue(gambler).await()
                Resource.Success(true)
            }
        }
    }

    suspend fun saveImageToStorage(path: StorageReference, uri: Uri): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeCall {
                path.putFile(uri).await()
                Resource.Success(true)
            }
        }
    }

    suspend fun getUrlFromStorage(path: StorageReference): Resource<Uri> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val uri = path.downloadUrl.await()
                Resource.Success(uri)
            }
        }
    }

    suspend fun saveGamblerPhoto(id: String, photoUri: Uri): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_PHOTO).child(id)
                //path.delete().await()

                if (saveImageToStorage(path, photoUri).data == true) {
                    val uri = getUrlFromStorage(path)
                    REF_DB_ROOT.child(NODE_GAMBLERS).child(id).child(GAMBLER_PHOTO_URL).setValue(uri.data.toString()).await()

                    Resource.Success(true)
                } else {
                    Resource.Error("FirebaseRepository -> saveGamblerPhoto: error!!!", false)
                }
            }
        }
    }

    fun eventListenerGamblerLiveData(liveData: MutableLiveData<GamblerModel>) {
        listenerGambler = REF_DB_ROOT.child(NODE_GAMBLERS).child(CURRENT_ID)
            .addValueEventListener(AppValueEventListener {
                GAMBLER = it.getValue(GamblerModel::class.java) ?: GamblerModel()
                liveData.value = GAMBLER
            })
    }

    fun removeEventListener(listener: ValueEventListener) {
        REF_DB_ROOT.removeEventListener(listener)
        toLog("removeListenerGamblerLiveData: $listener")
    }
}