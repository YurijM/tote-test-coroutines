package com.example.tote_test.ui.tabs.rating

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
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
        val moveArrow: ImageView = view.findViewById(R.id.itemRatingMoveArrow)
        val photo: ImageView = view.findViewById(R.id.itemRatingGamblerPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rating, parent, false)
        return RatingHolder(view)
    }

    override fun onBindViewHolder(holder: RatingHolder, position: Int) {
        holder.nickname.text = gamblers[position].nickname

        holder.points.text = gamblers[position].points.toString()

        val color = when (gamblers[position].place) {
            1 -> R.color.red
            2 -> R.color.green
            3 -> R.color.navy
            else -> R.color.grey
        }

        holder.points.setTextColor(
            ResourcesCompat.getColor(
                APP_ACTIVITY.resources,
                color,
                null)
        )

        holder.movePlaces.text = (gamblers[position].placePrev - gamblers[position].place).toString()

        val metrics = APP_ACTIVITY.resources.displayMetrics.density
        val size = (APP_ACTIVITY.resources.getDimension(R.dimen.rating_size_gambler_photo) * metrics).roundToInt()
        val radius = (APP_ACTIVITY.resources.getDimension(R.dimen.profile_size_photo_radius) * metrics).roundToInt()

        holder.photo.loadImage(gamblers[position].photoUrl, size, size, radius)

        val movePlaces = gamblers[position].placePrev - gamblers[position].place

        if (gamblers[position].placePrev == 0) {
            holder.moveArrow.visibility = View.INVISIBLE
            holder.movePlaces.visibility = View.INVISIBLE
        } else {
            when {
                movePlaces > 0 -> {
                    holder.moveArrow.visibility = View.VISIBLE
                    holder.moveArrow.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            APP_ACTIVITY.resources,
                            R.drawable.ic_arrow_up,
                            null)
                    )
                    holder.movePlaces.visibility = View.VISIBLE
                    holder.movePlaces.text = movePlaces.toString()
                    holder.movePlaces.setTextColor(
                        ResourcesCompat.getColor(
                            APP_ACTIVITY.resources,
                            R.color.red,
                            null)
                    )
                }
                movePlaces < 0 -> {
                    holder.moveArrow.visibility = View.VISIBLE
                    holder.moveArrow.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            APP_ACTIVITY.resources,
                            R.drawable.ic_arrow_down,
                            null
                        )
                    )
                    holder.movePlaces.visibility = View.VISIBLE
                    holder.movePlaces.text = (movePlaces * (-1)).toString()
                    holder.movePlaces.setTextColor(
                        ResourcesCompat.getColor(
                            APP_ACTIVITY.resources,
                            R.color.black,
                            null))
                }
                else -> {
                    holder.moveArrow.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            APP_ACTIVITY.resources,
                            R.drawable.ic_arrows_compare,
                            null
                        )
                    )
                    holder.movePlaces.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun getItemCount(): Int = gamblers.size

    @SuppressLint("NotifyDataSetChanged")
    fun setGamblers(gamblers: List<GamblerModel>) {
        this.gamblers = gamblers
        notifyDataSetChanged()
    }
}