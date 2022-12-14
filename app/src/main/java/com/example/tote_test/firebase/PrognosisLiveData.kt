package com.example.tote_test.firebase

import androidx.lifecycle.LiveData
import com.example.tote_test.models.PrognosisModel
import com.example.tote_test.utils.NODE_PROGNOSIS
import com.example.tote_test.utils.REF_DB_ROOT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class PrognosisLiveData : LiveData<List<PrognosisModel>>() {
    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.children.map {
                it.getValue(PrognosisModel::class.java)?: PrognosisModel()
            }
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    override fun onActive() {
        super.onActive()
        REF_DB_ROOT.child(NODE_PROGNOSIS).addValueEventListener(listener)
    }

    override fun onInactive() {
        REF_DB_ROOT.child(NODE_PROGNOSIS).removeEventListener(listener)
        super.onInactive()
    }
}