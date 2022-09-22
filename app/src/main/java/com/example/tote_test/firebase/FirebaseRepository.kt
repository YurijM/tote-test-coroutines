package com.example.tote_test.firebase

import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.*
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseRepository {
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

                    toLog("REPOSITORY -> signUp -> result: $result")
                }
                Resource.Success(result)
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

    suspend fun getGambler(id: String): GamblerModel {
        var gambler = GamblerModel()

        try {
            toLog("id: $id")
            gambler = REF_DB_ROOT.child(NODE_GAMBLERS).child(id)
                .get()
                .await()
                .getValue(GamblerModel::class.java) ?: GamblerModel()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                fixError(e.toString())
            }
        }

        toLog("REPOSITORY -> gambler: $gambler")
        return gambler
    }
}