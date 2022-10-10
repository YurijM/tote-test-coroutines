package com.example.tote_test.utils

import android.annotation.SuppressLint
import android.text.Editable
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.tote_test.R
import com.example.tote_test.models.GamblerModel
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

fun showToast(msg: String) {
    Toast.makeText(APP_ACTIVITY, msg, Toast.LENGTH_LONG).show()
}

fun toLog(msg: String) {
    Log.d(LOG_TAG, msg)
}

fun fixError(err: String) {
    Log.e(LOG_TAG, err)
    showToast(err)
}

fun checkFieldBlank(
    input: String,
    layout: TextInputLayout,
    fieldName: String
): Boolean {
    var result = true

    if (input.isBlank()) {
        layout.isErrorEnabled = true
        layout.error = APP_ACTIVITY.getString(R.string.error_field_empty, fieldName)
    } else {
        layout.isErrorEnabled = false
        result = false
    }

    return result
}

fun checkMinLength(
    minValue: Int,
    input: Editable,
    layout: TextInputLayout,
    fieldName: String
): Boolean {
    var result = false

    if (input.length < minValue) {
        layout.isErrorEnabled = true
        layout.error = APP_ACTIVITY.getString(R.string.error_min_length, fieldName, minValue)
    } else {
        layout.isErrorEnabled = false
        result = true
    }

    return result
}

fun isProfileFilled(profile: GamblerModel): Boolean =
    !(profile.nickname.isBlank()
            || profile.name.isBlank()
            || profile.family.isBlank()
            || profile.gender.isBlank()
            || (profile.photoUrl.isBlank() || profile.photoUrl == "empty")
            || profile.stake == 0
            )

fun ImageView.loadImage(url: String) {
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(R.drawable.user)
        .into(this)
}

fun ImageView.loadImage(url: String, width: Int, height: Int, radius: Int = 0, margin: Int = 0) {
    val transformation = RoundedCornersTransformation(radius, margin)
    Picasso.get()
        .load(url)
        .resize(width, height)
        .transform(transformation)
        .centerCrop()
        .placeholder(R.drawable.user)
        .into(this)
}

@SuppressLint("PrivateResource")
fun loadAppBarPhoto() {
    val gamblerPhoto = APP_ACTIVITY.findViewById<ImageView>(R.id.gamblerPhoto)
    var size = APP_ACTIVITY.resources.getDimensionPixelSize(com.google.android.material.R.dimen.action_bar_size) * 2

    val tv = TypedValue()
    if (APP_ACTIVITY.theme.resolveAttribute(com.google.android.material.R.attr.actionBarSize, tv, true)) {
        val actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, APP_ACTIVITY.resources.displayMetrics)
        size = actionBarHeight - APP_ACTIVITY.resources.getDimensionPixelSize(R.dimen.padding_xl)
    }

    gamblerPhoto.loadImage(GAMBLER.photoUrl, size, size, size / 2)
    gamblerPhoto.visibility = View.VISIBLE
}

fun Fragment.findTopNavController(): NavController {
    val topLevelHost = requireActivity().supportFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}



