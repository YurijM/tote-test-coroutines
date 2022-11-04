package com.example.tote_test.models

import java.io.Serializable

data class StakeModel (
    val gameId: Int = 0,
    val gamblerId: String = "",
    val goal1: String = "",
    val goal2: String = "",
    val addGoal1: String = "",
    val addGoal2: String = "",
    val penalty: String = "",
) : Serializable