package com.example.tote_test.ui.tabs.rating

import android.annotation.SuppressLint
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


class RatingAdapter : RecyclerView.Adapter<RatingAdapter.RatingHolder>() {
    private var gamblers = emptyList<GamblerModel>()

    class RatingHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nickname: TextView = view.findViewById(R.id.itemRatingGamblerNickname)
        val points: TextView = view.findViewById(R.id.itemRatingPoints)
        val movePlaces: TextView = view.findViewById(R.id.itemRatingMovePlaces)
        val photo: ImageView = view.findViewById(R.id.itemRatingGamblerPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rating, parent, false)
        return RatingHolder(view)
    }

    override fun onBindViewHolder(holder: RatingHolder, position: Int) {
        holder.nickname.text = gamblers[position].nickname
        holder.points.text = gamblers[position].points.toString()
        holder.movePlaces.text = (gamblers[position].placePrev - gamblers[position].place).toString()

        val metrics = APP_ACTIVITY.resources.displayMetrics.density
        val size = (APP_ACTIVITY.resources.getDimension(R.dimen.rating_size_gambler_photo) * metrics).roundToInt()
        val radius = (APP_ACTIVITY.resources.getDimension(R.dimen.profile_size_photo_radius) * metrics).roundToInt()

        holder.photo.loadImage(gamblers[position].photoUrl, size, size, radius)
    }

    override fun getItemCount(): Int = gamblers.size

    @SuppressLint("NotifyDataSetChanged")
    fun setGamblers(gamblers: List<GamblerModel>) {
        this.gamblers = gamblers
        notifyDataSetChanged()
    }
}