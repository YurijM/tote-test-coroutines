package com.example.tote_test.ui.tabs.games

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.GroupGamesModel
import com.example.tote_test.models.GroupResultsModel
import com.example.tote_test.utils.*

class GamesAdapter :
    RecyclerView.Adapter<GamesAdapter.GamesHolder>() {
    private var games = emptyList<GroupGamesModel>()
    private lateinit var groupResults: ArrayList<GroupResultsModel>

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

        var startCell: Int
        var startScopeCell: Int
        var endScopeCell: Int
        var startResultCell = 0

        groupResults = arrayListOf(
            GroupResultsModel(),
            GroupResultsModel(),
            GroupResultsModel(),
            GroupResultsModel()
        )

        for (row in 1..GROUP_TABLE_ROWS_COUNT) {
            startCell = GROUP_TABLE_COLUMNS_COUNT * row
            startScopeCell = startCell + SCOPE_CELL_START + row
            endScopeCell = startScopeCell + GROUP_TEAMS_COUNT - 1 - row
            startResultCell = endScopeCell + 1

            var rowReverse = row

            val team = teams[row - 1].team

            var i = startCell

            var cell = holder.table.getChildAt(i++) as TextView
            cell.text = row.toString()

            cell = holder.table.getChildAt(i) as TextView
            cell.text = team

            var cellReverse = startScopeCell + GROUP_TABLE_COLUMNS_COUNT - 1

            if (row < GROUP_TABLE_ROWS_COUNT) {
                for (cellScope in startScopeCell..endScopeCell) {
                    val game = groupGames[idxGame]

                    cell = holder.table.getChildAt(cellScope) as TextView

                    var goal1 = ""
                    var goal2 = ""

                    var result = if (game.goal1.isBlank() || game.goal2.isBlank()) {
                        "-"
                    } else if (game.team1 == team) {
                        goal1 = game.goal1
                        goal2 = game.goal2
                        "${game.goal1} : ${game.goal2}"
                    } else {
                        goal1 = game.goal2
                        goal2 = game.goal1
                        "${game.goal2} : ${game.goal1}"
                    }

                    cell.text = result

                    if (result != "-") {
                        cell.setTextColor(
                            ResourcesCompat.getColor(
                                APP_ACTIVITY.resources,
                                calcResultAttr(goal1.toInt(), goal2.toInt(), groupResults[row - 1]),
                                null
                            )
                        )
                    }

                    cell = holder.table.getChildAt(cellReverse) as TextView
                    result = result.reversed()
                    cell.text = result

                    if (result != "-" && row < GROUP_TABLE_ROWS_COUNT) {
                        cell.setTextColor(
                            ResourcesCompat.getColor(
                                APP_ACTIVITY.resources,
                                calcResultAttr(goal2.toInt(), goal1.toInt(), groupResults[rowReverse]),
                                null
                            )
                        )
                    }

                    cellReverse += GROUP_TABLE_COLUMNS_COUNT
                    rowReverse++

                    idxGame++
                }

                setResultAttr(holder, startResultCell, row - 1)
            }
        }

        setResultAttr(holder, startResultCell, GROUP_TABLE_ROWS_COUNT - 1)
    }

    private fun calcResultAttr(goal1: Int, goal2: Int, result: GroupResultsModel): Int {
        val color: Int

        if (goal1 > goal2) {
            color = R.color.red
            result.win++
            result.points += 3
        } else if (goal1 < goal2) {
            color = R.color.navy
            result.defeat++
        } else {
            color = R.color.application
            result.draw++
            result.points += 1
        }

        result.ball1 += goal1
        result.ball2 += goal2

        return color
    }

    private fun setResultAttr(holder: GamesHolder, startCell: Int, row: Int) {
        var start = startCell

        var cell = holder.table.getChildAt(start++) as TextView
        var item: String = groupResults[row].win.toString()
        cell.text = item

        cell = holder.table.getChildAt(start++) as TextView
        item = groupResults[row].draw.toString()
        cell.text = item

        cell = holder.table.getChildAt(start++) as TextView
        item = groupResults[row].defeat.toString()
        cell.text = item

        cell = holder.table.getChildAt(start++) as TextView
        item = "${groupResults[row].ball1}:${groupResults[row].ball2}"
        cell.text = item

        cell = holder.table.getChildAt(start) as TextView
        item = groupResults[row].points.toString()
        cell.text = item
    }

    override fun getItemCount(): Int = games.size

    @SuppressLint("NotifyDataSetChanged")
    fun setGames(games: List<GroupGamesModel>) {
        this.games = games
        notifyDataSetChanged()
    }
}