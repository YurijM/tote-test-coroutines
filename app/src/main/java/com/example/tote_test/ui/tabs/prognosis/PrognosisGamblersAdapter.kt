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
        val nickname: TextView = view.findViewById(R.id.itemPrognosisGamblerNik)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrognosisGamblersHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_prognosis_gambler, parent, false)
        return PrognosisGamblersHolder(view)
    }

    override fun onBindViewHolder(holder: PrognosisGamblersHolder, position: Int) {
        holder.nickname.text = prognosisGamblers[position].gamblerId
        //toLog("holder.table.size: ${holder.table.size}")
        /*val cell = holder.table.getChildAt(0) as TextView
        cell.text = prognosisGamblers.size.toString()*/
    }

    override fun getItemCount(): Int = prognosisGamblers.size

    @SuppressLint("NotifyDataSetChanged")
    fun setPrognosis(prognosisGamblers: List<StakeModel>) {
        this.prognosisGamblers = prognosisGamblers
        notifyDataSetChanged()
    }
}