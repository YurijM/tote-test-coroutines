package com.example.tote_test.models

import com.example.tote_test.utils.EMPTY
import java.io.Serializable

data class WinnerModel(
    var id: String = "",
    var gender: String = "",
    val photoUrl: String = EMPTY, // для Picasso поле не должно быть пустым
    var place: Int = 0,
    var stake: Int = 0,
    var winning: Double = 0.00,
) : Serializable