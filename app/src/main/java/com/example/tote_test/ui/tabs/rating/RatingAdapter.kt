package com.example.tote_test.ui.tabs.rating

import android.annotation.SuppressLint
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.APP_ACTIVITY
import com.example.tote_test.utils.loadImage
import com.example.tote_test.utils.toLog
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: RatingHolder, position: Int) {
        holder.nickname.text = gamblers[position].nickname

        holder.points.text = gamblers[position].points.toString()

        /*val value = TypedValue()
        APP_ACTIVITY.theme.resolveAttribute(R.attr.color_third_place, value, true)
        getView().setBackgroundColor(value.data)*/

        /*val colorRes = TypedValue().let {
            APP_ACTIVITY.theme.resolveAttribute(R.styleable.ds_color_third_place, it, true)
            APP_ACTIVITY.getColor(it.resourceId)
        }
        val color1 = ResourcesCompat.getColor(
            APP_ACTIVITY.resources,
            colorRes,
            null)*/

        //val hexColor = "#" + Integer.toHexString(value.data).substring(2)

        //parseColor("#000080").green

        toLog("color1: ${R.attr.color_third_place}")
        //toLog("hexColor: $hexColor")
        toLog("navy: ${R.color.navy}")

        /*val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.color_third_place, typedValue, true);
        val color11 = ContextCompat.getColor(APP_ACTIVITY, typedValue.resourceId)*/

        /*val color = when (gamblers[position].place) {
            1 -> R.color.red
            2 -> R.color.green
            3 -> R.color.navy
            else -> R.color.grey
        }*/

        /*holder.points.setTextColor(
            ResourcesCompat.getColor(
                APP_ACTIVITY.resources,
                color,
                null)
        )*/

        val value = TypedValue()

        when (gamblers[position].place) {
            1 -> APP_ACTIVITY.theme.resolveAttribute(R.attr.color_first_place, value, true)
            2 -> APP_ACTIVITY.theme.resolveAttribute(R.attr.color_second_place, value, true)
            3 -> APP_ACTIVITY.theme.resolveAttribute(R.attr.color_third_place, value, true)
            else -> APP_ACTIVITY.theme.resolveAttribute(R.attr.color_other_place, value, true)
        }

        holder.points.setTextColor(value.data)

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