package com.example.tote_test.ui.tabs.prognosis

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.StakeModel

class PrognosisGamblersAdapter : RecyclerView.Adapter<PrognosisGamblersAdapter.PrognosisGamblersHolder>() {
    private var stakes = emptyList<StakeModel>()

    class PrognosisGamblersHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nickname: TextView = view.findViewById(R.id.itemPrognosisGamblerNickname)
        val stake: TextView = view.findViewById(R.id.itemPrognosisGamblerStake)
        val points: TextView = view.findViewById(R.id.itemPrognosisGamblerPoints)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrognosisGamblersHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_prognosis_gambler, parent, false)
        return PrognosisGamblersHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PrognosisGamblersHolder, position: Int) {
        val stake = stakes[position]

        holder.nickname.text = stake.gamblerId

        var result = "-"

        if (stake.goal1.isNotBlank() && stake.goal2.isNotBlank()) {
            result = "${stake.goal1}:${stake.goal2}"

            if (stake.addGoal1.isNotBlank()) {
                result += ", доп.время ${stake.addGoal1}:${stake.addGoal2}"

                if (stake.penalty.isNotBlank()) {
                    result += ", по пенальти ${stake.penalty}"
                }
            }
        }

        holder.stake.text = result

        holder.points.text = "%.2f".format(stake.points)
    }

    override fun getItemCount(): Int = stakes.size

    @SuppressLint("NotifyDataSetChanged")
    fun setStakes(stakes: List<StakeModel>) {
        this.stakes = stakes
        notifyDataSetChanged()
    }
}