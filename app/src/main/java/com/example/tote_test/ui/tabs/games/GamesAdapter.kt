package com.example.tote_test.ui.tabs.games

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.GameModel

class GamesAdapter(private val onItemClicked: (game: GameModel) -> Unit) :
    RecyclerView.Adapter<GamesAdapter.RatingHolder>() {
    private var games = emptyList<GameModel>()

    class RatingHolder(
        view: View,
        private val onItemClicked: (game: GameModel) -> Unit
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val group: TextView = view.findViewById(R.id.groupGamesGroup)
        val table: ConstraintLayout = view.findViewById(R.id.groupGamesTable)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val game = v.tag as GameModel
            onItemClicked(game)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rating, parent, false)
        return RatingHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: RatingHolder, position: Int) {
        holder.itemView.tag = games[position]

        val group = "Группа ${games[position].group}"

        holder.group.text = group

        val cellsCount = holder.table.childCount

        for (i in 0 until cellsCount) {
            val cell = holder.table.getChildAt(i) as TextView
            cell.text = i.toString()
        }
    }

    override fun getItemCount(): Int = games.size

    @SuppressLint("NotifyDataSetChanged")
    fun setGames(games: List<GameModel>) {
        this.games = games
        notifyDataSetChanged()
    }
}