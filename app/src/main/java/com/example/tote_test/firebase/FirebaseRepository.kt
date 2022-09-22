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

    suspend fun signIn(): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = AUTH.signInWithEmailAndPassword(EMAIL, PASSWORD).await()
                CURRENT_ID = AUTH.currentUser?.uid.toString()
                Resource.Success(result)
            }
        }
    }

    suspend fun getGambler(): GamblerModel {
        var gambler = GamblerModel()

        try {
            toLog("CURRENT_ID: $CURRENT_ID")
            gambler = REF_DB_ROOT.child(NODE_GAMBLERS).child(CURRENT_ID)
                .get()
                .await()
                .getValue(GamblerModel::class.java) ?: GamblerModel()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                fixError(e.toString())
            }
        }

        toLog("REPOSITORY -> GAMBLER: $gambler")
        return gambler
    }
}