package com.example.tote_test.ui.tabs.stakes.stake

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.GameModel
import com.example.tote_test.utils.APP_ACTIVITY

class StakeAdapter : RecyclerView.Adapter<StakeAdapter.StakeHolder>() {
    private var games = emptyList<GameModel>()

    inner class StakeHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout: ConstraintLayout = view.findViewById(R.id.playedGamesLayout)
        val team1: TextView = view.findViewById(R.id.playedGamesTeam1)
        val team2: TextView = view.findViewById(R.id.playedGamesTeam2)
        val goal1: TextView = view.findViewById(R.id.playedGamesGoal1)
        val goal2: TextView = view.findViewById(R.id.playedGamesGoal2)
        val addTime: TextView = view.findViewById(R.id.playedGamesAddTime)
        val colon: TextView = view.findViewById(R.id.playedGamesColon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StakeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_played_games, parent, false)
        return StakeHolder(view)
    }

    override fun onBindViewHolder(holder: StakeHolder, position: Int) {
        val game = games[position]

        with(holder) {
            addTime.isGone = true

            if (game.id == 0) {
                colon.isGone = true
                layout.layoutParams.height = 12
            }

            team1.text = game.team1
            goal1.text = game.goal1
            team2.text = game.team2
            goal2.text = game.goal2

            if (game.addGoal1.isNotBlank() && game.goal2.isNotBlank() && game.goal1 == game.goal2) {
                var addTime = APP_ACTIVITY.getString(
                    R.string.stake_add_time,
                    game.addGoal1,
                    game.addGoal2
                )

                if (game.penalty.isNotBlank() && game.addGoal1 == game.addGoal2) {
                    addTime += APP_ACTIVITY.getString(
                        R.string.stake_penalty,
                        game.penalty
                    )
                }

                holder.addTime.text = addTime
                holder.addTime.isVisible = true
            }
        }
    }

    override fun getItemCount(): Int = games.size

    @SuppressLint("NotifyDataSetChanged")
    fun setPlayedGames(games: List<GameModel>) {
        this.games = games
        notifyDataSetChanged()
    }

}