package com.example.tote_test.firebase

import androidx.lifecycle.LiveData
import com.example.tote_test.models.GameModel
import com.example.tote_test.utils.NODE_GAMES
import com.example.tote_test.utils.REF_DB_ROOT
import com.example.tote_test.utils.toLog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class GamesLiveData: LiveData<List<GameModel>>() {
    private val listener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.children.map {
                it.getValue(GameModel::class.java)?: GameModel()
            }
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    override fun onActive() {
        super.onActive()
        REF_DB_ROOT.child(NODE_GAMES).addValueEventListener(listener)
        REF_DB_ROOT.child(NODE_GAMES).keepSynced(true)
        toLog("games onActive")
    }

    override fun onInactive() {
        REF_DB_ROOT.child(NODE_GAMES).removeEventListener(listener)
        REF_DB_ROOT.child(NODE_GAMES).keepSynced(false)
        toLog("games onInactive")
        super.onInactive()
    }
}