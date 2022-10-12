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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tote_test.R
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.APP_ACTIVITY
import com.example.tote_test.utils.GAMBLER
import com.example.tote_test.utils.loadImage
import kotlin.math.roundToInt

class RatingAdapter(private val onItemClicked: (gambler: GamblerModel) -> Unit) :
    RecyclerView.Adapter<RatingAdapter.RatingHolder>() {
    private var gamblers = emptyList<GamblerModel>()

    class RatingHolder(
        view: View,
        private val onItemClicked: (gambler: GamblerModel) -> Unit
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val nickname: TextView = view.findViewById(R.id.itemRatingGamblerNickname)
        val photo: ImageView = view.findViewById(R.id.itemRatingGamblerPhoto)
        val groupRating: ConstraintLayout = view.findViewById(R.id.itemRatingGroupRating)
        val points: TextView = view.findViewById(R.id.itemRatingPoints)
        val movePlaces: TextView = view.findViewById(R.id.itemRatingMovePlaces)
        val moveArrow: ImageView = view.findViewById(R.id.itemRatingMoveArrow)

        init {
            if (GAMBLER.admin) {
                view.setOnClickListener(this)
            }/* else {
                view.setOnClickListener(null)
            }*/
        }

        override fun onClick(v: View) {
            val gambler = v.tag as GamblerModel
            onItemClicked(gambler)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rating, parent, false)
        return RatingHolder(view, onItemClicked)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: RatingHolder, position: Int) {
        holder.itemView.tag = gamblers[position]

        holder.nickname.text = gamblers[position].nickname

        loadPhoto(holder.photo, gamblers[position].photoUrl)

        if (gamblers[position].active) {
            holder.groupRating.visibility = View.VISIBLE

            holder.points.setTextColor(setPointColor(gamblers[position].place))
            holder.points.text = "%.2f".format(gamblers[position].points)

            setMovePlaces(holder, gamblers[position].placePrev, gamblers[position].place)
        } else {
            holder.groupRating.visibility = View.INVISIBLE
        }
    }

    private fun setMovePlaces(holder: RatingHolder, placePrev: Int, place: Int) {
        val movePlaces = placePrev - place

        holder.movePlaces.text = movePlaces.toString()

        if (placePrev == 0) {
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
                            null
                        )
                    )
                    holder.movePlaces.visibility = View.VISIBLE
                    holder.movePlaces.setTextColor(
                        ResourcesCompat.getColor(
                            APP_ACTIVITY.resources,
                            R.color.red,
                            null
                        )
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
                            null
                        )
                    )
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

    private fun setPointColor(place: Int): Int {
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
        val size = (APP_ACTIVITY.resources.getDimension(R.dimen.rating_size_gambler_photo) * metrics).roundToInt()
        val radius = (APP_ACTIVITY.resources.getDimension(R.dimen.profile_size_photo_radius) * metrics).roundToInt()

        photo.loadImage(url, size, size, radius)
    }

    override fun getItemCount(): Int = gamblers.size

    @SuppressLint("NotifyDataSetChanged")
    fun setGamblers(gamblers: List<GamblerModel>) {
        this.gamblers = gamblers
        notifyDataSetChanged()
    }
}