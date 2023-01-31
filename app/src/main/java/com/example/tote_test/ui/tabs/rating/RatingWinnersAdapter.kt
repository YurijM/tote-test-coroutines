package com.example.tote_test.ui.tabs.rating

import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.APP_ACTIVITY
import com.example.tote_test.utils.loadImage
import kotlin.math.roundToInt

class RatingWinnersAdapter :
    RecyclerView.Adapter<RatingWinnersAdapter.RatingWinnersHolder>() {
    private var winners = emptyList<GamblerModel>()

    class RatingWinnersHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {
        val photo: ImageView = view.findViewById(R.id.itemWinnerPhoto)
        val nickname: TextView = view.findViewById(R.id.itemWinnerNickname)
        val winning: TextView = view.findViewById(R.id.itemWinnerWinning)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingWinnersHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_winner, parent, false)
        return RatingWinnersHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RatingWinnersHolder, position: Int) {
        val winner = winners[position]

        loadPhoto(holder.photo, winner.photoUrl)

        holder.nickname.text = winner.nickname

        holder.winning.setTextColor(setWinningColor(winner.place))
        holder.winning.text = "%.2f".format(winner.points)
    }

    private fun setWinningColor(place: Int): Int {
        val value = TypedValue()

        when (place) {
            1 -> APP_ACTIVITY.theme.resolveAttribute(R.attr.color_first_place, value, true)
            2 -> APP_ACTIVITY.theme.resolveAttribute(R.attr.color_second_place, value, true)
            3 -> APP_ACTIVITY.theme.resolveAttribute(R.attr.color_third_place, value, true)
            else -> APP_ACTIVITY.theme.resolveAttribute(R.attr.color_other_place, value, true)
        }

        return value.data
    }

    private fun loadPhoto(photo: ImageView, url: String) {
        val metrics = APP_ACTIVITY.resources.displayMetrics.density
        val size = (APP_ACTIVITY.resources.getDimension(R.dimen.winner_size_photo) * metrics).roundToInt()

        photo.loadImage(url, size, size)
    }

    override fun getItemCount(): Int = winners.size

    @SuppressLint("NotifyDataSetChanged")
    fun setWinners(winners: List<GamblerModel>) {
        this.winners = winners
        notifyDataSetChanged()
    }
}