package com.example.tote_test.models

import com.example.tote_test.utils.EMPTY
import java.io.Serializable

data class TeamModel(
    var team: String = "",
    var group: String = "",
    val flagUrl: String = EMPTY,
    val sequenceNumber: Int
) : Serializable