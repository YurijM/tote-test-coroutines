package com.example.tote_test.firebase

import androidx.lifecycle.LiveData
import com.example.tote_test.models.TeamModel
import com.example.tote_test.utils.NODE_TEAMS
import com.example.tote_test.utils.REF_DB_ROOT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TeamsLiveData : LiveData<List<TeamModel>>() {
    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.children.map {
                it.getValue(TeamModel::class.java) ?: TeamModel()
            }
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    override fun onActive() {
        super.onActive()
        REF_DB_ROOT.child(NODE_TEAMS).addValueEventListener(listener)
    }

    override fun onInactive() {
        REF_DB_ROOT.child(NODE_TEAMS).removeEventListener(listener)
        super.onInactive()
    }
}