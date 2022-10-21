package com.example.tote_test.ui.tabs.games

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.GameModel
import com.example.tote_test.models.GroupGamesModel

//class GamesAdapter(private val onItemClicked: (game: GameModel) -> Unit) :
class GamesAdapter() :
    RecyclerView.Adapter<GamesAdapter.GamesHolder>() {
    private var games = emptyList<GroupGamesModel>()

    class GamesHolder(
        view: View,
        //private val onItemClicked: (game: GameModel) -> Unit
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val group: TextView = view.findViewById(R.id.groupGamesGroup)
        val table: GridLayout = view.findViewById(R.id.groupGamesTable)

        /*init {
            view.setOnClickListener(this)
        }*/

        /*override fun onClick(v: View) {
            val game = v.tag as GameModel
            onItemClicked(game)
        }*/
        override fun onClick(v: View) {}
}

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesHolder {
val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group_games, parent, false)
return GamesHolder(view) //, onItemClicked)
}

override fun onBindViewHolder(holder: GamesHolder, position: Int) {
holder.itemView.tag = games[position]

val group = "Группа ${games[position].group}"

holder.group.text = group

val cellsCount = holder.table.childCount

/*for (i in 0 until cellsCount) {
    val cell = holder.table.getChildAt(i) as TextView
    cell.text = i.toString()
}*/
}

override fun getItemCount(): Int = games.size

@SuppressLint("NotifyDataSetChanged")
/*fun setGames(games: List<GameModel>) {
this.games = games
notifyDataSetChanged()
}*/
fun setGames(games: List<GroupGamesModel>) {
this.games = games
notifyDataSetChanged()
}
}