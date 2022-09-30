package com.example.tote_test.firebase

import androidx.lifecycle.LiveData
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.NODE_GAMBLERS
import com.example.tote_test.utils.REF_DB_ROOT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class GamblersLiveData: LiveData<List<GamblerModel>>() {
    private val listener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.children.map {
                it.getValue(GamblerModel::class.java)?: GamblerModel()
            }
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    override fun onActive() {
        super.onActive()
        REF_DB_ROOT.child(NODE_GAMBLERS).addValueEventListener(listener)
    }

    override fun onInactive() {
        REF_DB_ROOT.child(NODE_GAMBLERS).removeEventListener(listener)
        super.onInactive()
    }
}