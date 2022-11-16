package com.example.tote_test.firebase

import androidx.lifecycle.LiveData
import com.example.tote_test.models.EmailModel
import com.example.tote_test.utils.NODE_EMAILS
import com.example.tote_test.utils.REF_DB_ROOT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class EmailsLiveData: LiveData<List<EmailModel>>() {
    private val listener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.children.map {
                it.getValue(EmailModel::class.java)?: EmailModel()
            }
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    override fun onActive() {
        super.onActive()
        REF_DB_ROOT.child(NODE_EMAILS).addValueEventListener(listener)
    }

    override fun onInactive() {
        REF_DB_ROOT.child(NODE_EMAILS).removeEventListener(listener)
        super.onInactive()
    }
}