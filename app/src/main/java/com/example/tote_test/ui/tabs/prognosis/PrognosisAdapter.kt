package com.example.tote_test.ui.tabs.prognosis

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.GameStakesModel

class PrognosisAdapter : RecyclerView.Adapter<PrognosisAdapter.PrognosisHolder>() {
    private var prognosis = emptyList<GameStakesModel>()

    class PrognosisHolder(view: View) : RecyclerView.ViewHolder(view) {
        val game: TextView = view.findViewById(R.id.itemPrognosisGame)
        val gamblers: RecyclerView = view.findViewById(R.id.itemPrognosisGamblersList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrognosisHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_prognosis, parent, false)
        return PrognosisHolder(view)
    }

    override fun onBindViewHolder(holder: PrognosisHolder, position: Int) {
        holder.game.text = prognosis[position].game

        holder.gamblers.apply {
            val gamblersAdapter = PrognosisGamblersAdapter()
            gamblersAdapter.setPrognosis(prognosis[position].stakes)
            adapter = gamblersAdapter
        }
    }

    override fun getItemCount(): Int = prognosis.size

    @SuppressLint("NotifyDataSetChanged")
    fun setPrognosis(prognosis: List<GameStakesModel>) {
        this.prognosis = prognosis
        notifyDataSetChanged()
    }
}