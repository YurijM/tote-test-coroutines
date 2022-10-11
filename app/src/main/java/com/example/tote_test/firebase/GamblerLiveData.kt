package com.example.tote_test.firebase

import androidx.lifecycle.LiveData
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class GamblerLiveData : LiveData<GamblerModel>() {
    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.getValue(GamblerModel::class.java) ?: GamblerModel()
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    override fun onActive() {
        super.onActive()
        REF_DB_ROOT.child(NODE_GAMBLERS).child(CURRENT_ID).addValueEventListener(listener)
    }

    override fun onInactive() {
        REF_DB_ROOT.child(NODE_GAMBLERS).child(CURRENT_ID).removeEventListener(listener)
        super.onInactive()
    }
}