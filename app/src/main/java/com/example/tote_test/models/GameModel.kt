package com.example.tote_test.models

import java.io.Serializable

data class GameModel(
    var id: Int,
    var group: String = "",
    var team1: String = "",
    var team2: String = "",
    var start: String = "",
    var goal1: Int? = null,
    var goal2: Int? = null,
    var addGoal1: Int? = null,
    var addGoal2: Int? = null,
    val penalty: String = "",
) : Serializable