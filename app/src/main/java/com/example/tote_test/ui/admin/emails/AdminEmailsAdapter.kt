package com.example.tote_test.ui.admin.emails

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.EmailModel

class AdminEmailsAdapter(private val onItemClicked: (EmailModel) -> Unit) :
    RecyclerView.Adapter<AdminEmailsAdapter.AdminEmailsHolder>() {
    private var emails = emptyList<EmailModel>()

    class AdminEmailsHolder(
        view: View,
        private val onItemClicked: (EmailModel) -> Unit
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val email: TextView = view.findViewById(R.id.adminEmail)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val email = v.tag as EmailModel
            onItemClicked(email)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminEmailsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_email, parent, false)
        return AdminEmailsHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: AdminEmailsHolder, position: Int) {
        holder.itemView.tag = emails[position]

        holder.email.text = emails[position].email
    }

    override fun getItemCount(): Int = emails.size

    @SuppressLint("NotifyDataSetChanged")
    fun setEmails(emails: List<EmailModel>) {
        this.emails = emails
        notifyDataSetChanged()
    }

}