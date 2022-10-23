package com.example.tote_test.ui.tabs.games

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.GroupGamesModel
import com.example.tote_test.utils.*

class GamesAdapter :
    RecyclerView.Adapter<GamesAdapter.GamesHolder>() {
    private var games = emptyList<GroupGamesModel>()

    class GamesHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val group: TextView = view.findViewById(R.id.groupGamesGroup)
        val table: GridLayout = view.findViewById(R.id.groupGamesTable)

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

        val teams = TEAMS.filter { it.group == games[position].group }
            .sortedBy { item -> item.team }

        val groupGames = games[position].games
        var idxGame = 0

        //val cellsCount = holder.table.childCount
        /*var startCell = GROUP_TABLE_COLUMNS_COUNT
        var startScopeCell = startCell + SCOPE_CELL_START
        var endScopeCell = startCell + SCOPE_CELL_START + GROUP_TEAMS_COUNT*/

        for (row in 1 .. GROUP_TABLE_ROWS_COUNT) {
            val startCell = GROUP_TABLE_COLUMNS_COUNT * row
            val startScopeCell = startCell + SCOPE_CELL_START + row
            val endScopeCell = startScopeCell + GROUP_TEAMS_COUNT - 1 - row

            val team = teams[row - 1].team

            var i = startCell

            var cell = holder.table.getChildAt(i++) as TextView
            cell.text = row.toString()

            cell = holder.table.getChildAt(i) as TextView
            cell.text = team

            var cellReverse = startScopeCell + GROUP_TABLE_COLUMNS_COUNT - 1

            if (row < GROUP_TABLE_ROWS_COUNT) {
                for (cellScope in startScopeCell ..endScopeCell) {
                    val game = groupGames[idxGame]

                    cell = holder.table.getChildAt(cellScope) as TextView

                    val result = if (game.goal1.isBlank() || game.goal2.isBlank()) {
                        "-"
                    } else if (game.team1 == team) {
                        "${game.goal1} : ${game.goal2}"
                    } else {
                        "${game.goal2} : ${game.goal1}"
                    }

                    cell.text = result

                    cell = holder.table.getChildAt(cellReverse) as TextView
                    cell.text = result.reversed()

                    cellReverse += GROUP_TABLE_COLUMNS_COUNT

                    idxGame++
                }
            }
        }
    }

    override fun getItemCount(): Int = games.size

    @SuppressLint("NotifyDataSetChanged")
    fun setGames(games: List<GroupGamesModel>) {
        this.games = games
        notifyDataSetChanged()
    }
}