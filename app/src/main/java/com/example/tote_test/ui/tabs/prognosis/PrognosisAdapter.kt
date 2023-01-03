package com.example.tote_test.ui.tabs.prognosis

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.PrognosisModel
import com.example.tote_test.models.StakeModel
import com.example.tote_test.utils.APP_ACTIVITY

class PrognosisAdapter : RecyclerView.Adapter<PrognosisAdapter.PrognosisHolder>() {
    private var prognosis = emptyList<PrognosisModel>()
    private var stakes = emptyList<StakeModel>()

    class PrognosisHolder(view: View) : RecyclerView.ViewHolder(view) {
        val game: TextView = view.findViewById(R.id.itemPrognosisGame)
        val coefficientForWin: TextView = view.findViewById(R.id.itemPrognosisCoefficientForWin)
        val coefficientForDraw: TextView = view.findViewById(R.id.itemPrognosisCoefficientForDraw)
        val coefficientForDefeat: TextView = view.findViewById(R.id.itemPrognosisCoefficientForDefeat)
        val coefficientForFine: TextView = view.findViewById(R.id.itemPrognosisCoefficientForFine)
        val stakes: RecyclerView = view.findViewById(R.id.itemStakesList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrognosisHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_prognosis, parent, false)
        return PrognosisHolder(view)
    }

    override fun onBindViewHolder(holder: PrognosisHolder, position: Int) {
        val prognosisGambler = prognosis[position]

        holder.game.text = prognosisGambler.game

        holder.coefficientForWin.text = APP_ACTIVITY.getString(
            R.string.coefficient_for_win,
            prognosisGambler.coefficientForWin
        )
        holder.coefficientForDraw.text = APP_ACTIVITY.getString(
            R.string.coefficient_for_draw,
            prognosisGambler.coefficientForDraw
        )
        holder.coefficientForDefeat.text = APP_ACTIVITY.getString(
            R.string.coefficient_for_defeat,
            prognosisGambler.coefficientForDefeat
        )
        holder.coefficientForFine.text = APP_ACTIVITY.getString(
            R.string.coefficient_for_fine,
            prognosisGambler.coefficientForFine
        )

        holder.stakes.apply {
            val gamblersAdapter = PrognosisGamblersAdapter()
            //prognosisGambler.stakes?.let { gamblersAdapter.setPrognosis(it) }
            val stakesGame = stakes.filter { stake -> stake.gameId == prognosisGambler.gameId }
            gamblersAdapter.setStakes(stakesGame)
            adapter = gamblersAdapter
        }
    }

    override fun getItemCount(): Int = prognosis.size

    @SuppressLint("NotifyDataSetChanged")
    fun setPrognosis(prognosis: List<PrognosisModel>) {
        this.prognosis = prognosis
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setStakes(stakes: List<StakeModel>) {
        this.stakes = stakes
        notifyDataSetChanged()
    }
}