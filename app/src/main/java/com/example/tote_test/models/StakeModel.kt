package com.example.tote_test.models

import java.io.Serializable

data class StakeModel (
    var gameId: Int = 0,
    var gamblerId: String = "",
    var goal1: String = "",
    var goal2: String = "",
    var addGoal1: String = "",
    var addGoal2: String = "",
    var penalty: String = "",
) : Serializable