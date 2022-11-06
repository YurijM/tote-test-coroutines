package com.example.tote_test.ui.tabs.stakes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.GameModel
import com.example.tote_test.utils.APP_ACTIVITY
import com.example.tote_test.utils.GROUPS
import com.example.tote_test.utils.GROUPS_COUNT
import com.example.tote_test.utils.asTime

class StakesAdapter(private val onItemClicked: (game: GameModel) -> Unit) :
    RecyclerView.Adapter<StakesAdapter.StakesHolder>() {
    private var games = emptyList<GameModel>()

    class StakesHolder(
        view: View,
        private val onItemClicked: (game: GameModel) -> Unit
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val start: TextView = view.findViewById(R.id.itemStakeStart)
        val team1: TextView = view.findViewById(R.id.itemStakeTeam1)
        val goal1: TextView = view.findViewById(R.id.itemStakeGoal1)
        val team2: TextView = view.findViewById(R.id.itemStakeTeam2)
        val goal2: TextView = view.findViewById(R.id.itemStakeGoal2)
        val layoutAddTime: LinearLayout = view.findViewById(R.id.itemStakeLayoutAddTime)
        val addTime: TextView = view.findViewById(R.id.itemStakeAddTime)
        val penalty: TextView = view.findViewById(R.id.itemStakePenalty)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val game = v.tag as GameModel
            onItemClicked(game)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StakesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stake, parent, false)
        return StakesHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: StakesHolder, position: Int) {
        val game = games[position]
        holder.itemView.tag = game

        holder.layoutAddTime.isGone = true
        holder.penalty.isGone = true

        holder.start.text = APP_ACTIVITY.getString(
            R.string.start_game,
            game.start.asTime(false),
            game.id.toString()
        )
        holder.team1.text = game.team1
        holder.goal1.text = game.goal1.ifBlank { "?" }
        holder.team2.text = game.team2
        holder.goal2.text = game.goal2.ifBlank { "?" }

        val group = GROUPS.first { it.group == game.group }

        if (group.number > GROUPS_COUNT) {
            if (game.goal1.isNotBlank() && game.goal2.isNotBlank() && game.goal1 == game.goal2) {
                holder.layoutAddTime.isVisible = true
                holder.addTime.isVisible = true

                holder.addTime.text = APP_ACTIVITY.getString(
                    R.string.stake_add_time,
                    game.addGoal1,
                    game.addGoal2
                )
            }

            if (game.penalty.isNotBlank()) {
                holder.penalty.text = APP_ACTIVITY.getString(
                    R.string.stake_penalty,
                    game.penalty
                )

                holder.penalty.isVisible = true
            } else {
                holder.penalty.isGone = true
            }
        } else {
            holder.layoutAddTime.isGone = true
        }
    }

    override fun getItemCount(): Int = games.size

    @SuppressLint("NotifyDataSetChanged")
    fun setStakes(games: List<GameModel>) {
        this.games = games
        notifyDataSetChanged()
    }
}