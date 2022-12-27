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
    private var prognosisGamblers = emptyList<StakeModel>()

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
        val prognosis = prognosisGamblers[position]

        holder.nickname.text = prognosis.gamblerId

        var stake = "-"

        if (prognosis.goal1.isNotBlank() && prognosis.goal2.isNotBlank()) {
            stake = "${prognosis.goal1}:${prognosis.goal2}"

            if (prognosis.addGoal1.isNotBlank()) {
                stake += ", доп.время ${prognosis.addGoal1}:${prognosis.addGoal2}"

                if (prognosis.penalty.isNotBlank()) {
                    stake += ", по пенальти ${prognosis.penalty}"
                }
            }
        }

        holder.stake.text = stake

        holder.points.text = "%.2f".format(prognosis.points)
    }

    override fun getItemCount(): Int = prognosisGamblers.size

    @SuppressLint("NotifyDataSetChanged")
    fun setPrognosis(prognosisGamblers: List<StakeModel>) {
        this.prognosisGamblers = prognosisGamblers
        notifyDataSetChanged()
    }
}