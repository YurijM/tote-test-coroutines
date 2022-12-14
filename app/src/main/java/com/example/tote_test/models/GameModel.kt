package com.example.tote_test.models

import java.io.Serializable

data class GameModel(
    var id: Int = 0,
    var group: String = "",
    var team1: String = "",
    var team2: String = "",
    var start: String = "",
    var goal1: String = "",
    var goal2: String = "",
    var addGoal1: String = "",
    var addGoal2: String = "",
    var penalty: String = "",
) : Serializable