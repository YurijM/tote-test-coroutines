package com.example.tote_test.firebase

import androidx.lifecycle.LiveData
import com.example.tote_test.models.StakeModel
import com.example.tote_test.utils.NODE_STAKES
import com.example.tote_test.utils.REF_DB_ROOT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class StakesAllLiveData : LiveData<List<StakeModel>>() {
    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.map {
                value = it.children.map {stakes ->
                    stakes.getValue(StakeModel::class.java) ?: StakeModel()
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    override fun onActive() {
        super.onActive()
        REF_DB_ROOT.child(NODE_STAKES).addValueEventListener(listener)
    }

    override fun onInactive() {
        REF_DB_ROOT.child(NODE_STAKES).removeEventListener(listener)
        super.onInactive()
    }
}