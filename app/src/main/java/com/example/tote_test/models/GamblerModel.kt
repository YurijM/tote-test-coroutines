package com.example.tote_test.models

import com.example.tote_test.utils.EMPTY
import java.io.Serializable

data class GamblerModel(
    var id: String = "",
    var email: String = "",
    var nickname: String = "",
    var family: String = "",
    var name: String = "",
    var gender: String = "",
    val photoUrl: String = EMPTY, // для Picasso поле не должно быть пустым
    var stake: Int = 0,
    val points: Double = 0.00,
    val placePrev: Int = 0,
    val place: Int = 0,
    var admin: Boolean = false,
    var active: Boolean = false,
) : Serializable