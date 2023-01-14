package com.example.tote_test.ui.admin.group_games

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.GameModel
import com.example.tote_test.utils.APP_ACTIVITY
import com.example.tote_test.utils.asTime
import com.google.android.material.textfield.TextInputEditText

class AdminGroupGamesAdapter : RecyclerView.Adapter<AdminGroupGamesAdapter.AdminGroupGamesHolder>() {
    private var games = emptyList<GameModel>()

    private var listener: ((item: GameModel) -> Unit)? = null
    private var listenerStart: ((item: GameModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (item: GameModel) -> Unit) {
        this.listener = listener
    }

    fun setOnStartClickListener(listenerStart: (item: GameModel) -> Unit) {
        this.listenerStart = listenerStart
    }

    class AdminGroupGamesHolder(view: View) : RecyclerView.ViewHolder(view) {
        val start: TextView = view.findViewById(R.id.adminGroupGameStart)
        val team1: TextView = view.findViewById(R.id.adminGroupGameTeam1)
        val goal1: TextInputEditText = view.findViewById(R.id.adminGroupGameInputGoal1)
        val team2: TextView = view.findViewById(R.id.adminGroupGameTeam2)
        val goal2: TextInputEditText = view.findViewById(R.id.adminGroupGameInputGoal2)
        val buttonSave: Button = view.findViewById(R.id.adminGroupGameSave)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminGroupGamesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_group_games, parent, false)
        return AdminGroupGamesHolder(view)
    }

    override fun onBindViewHolder(holder: AdminGroupGamesHolder, position: Int) {
        holder.start.text = APP_ACTIVITY.getString(
            R.string.start_game,
            games[position].start.asTime(false),
            games[position].id.toString()
        )
        holder.team1.text = games[position].team1
        holder.goal1.setText(games[position].goal1)
        holder.team2.text = games[position].team2
        holder.goal2.setText(games[position].goal2)

        holder.buttonSave.setOnClickListener {
            val game = games[position]

            game.goal1 = holder.goal1.text.toString()
            game.goal2 = holder.goal2.text.toString()

            listener?.invoke(this.games[position])
        }

        holder.start.setOnClickListener {
            listenerStart?.invoke(this.games[position])
        }
    }

    override fun getItemCount(): Int = games.size

    @SuppressLint("NotifyDataSetChanged")
    fun setGames(games: List<GameModel>) {
        this.games = games
        notifyDataSetChanged()
    }
}