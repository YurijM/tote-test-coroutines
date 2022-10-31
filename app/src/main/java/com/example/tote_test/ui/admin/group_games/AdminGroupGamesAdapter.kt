package com.example.tote_test.ui.admin.group_games

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.GameModel
import com.example.tote_test.utils.asTime
import com.google.android.material.textfield.TextInputEditText

class AdminGroupGamesAdapter : RecyclerView.Adapter<AdminGroupGamesAdapter.AdminGroupGamesHolder>() {
    private var games = emptyList<GameModel>()

    class AdminGroupGamesHolder(view: View) : RecyclerView.ViewHolder(view) {
        val start: TextView = view.findViewById(R.id.adminGroupGameStart)
        val team1: TextView = view.findViewById(R.id.adminGroupGameTeam1)
        val goal1: TextInputEditText = view.findViewById(R.id.adminGroupGameInputGoal1)
        val team2: TextView = view.findViewById(R.id.adminGroupGameTeam2)
        val goal2: TextInputEditText = view.findViewById(R.id.adminGroupGameInputGoal2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminGroupGamesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_group_games, parent, false)
        return AdminGroupGamesHolder(view)
    }

    override fun onBindViewHolder(holder: AdminGroupGamesHolder, position: Int) {
        holder.start.text = games[position].start.asTime(false)
        holder.team1.text = games[position].team1
        holder.goal1.setText(games[position].goal1)
        holder.team2.text = games[position].team2
        holder.team2.text = games[position].team2
        holder.goal2.setText(games[position].goal2)
    }

    override fun getItemCount(): Int = games.size

    @SuppressLint("NotifyDataSetChanged")
    fun setGames(games: List<GameModel>) {
        this.games = games
        notifyDataSetChanged()
    }
}