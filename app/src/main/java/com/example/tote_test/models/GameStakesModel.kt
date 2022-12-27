package com.example.tote_test.models

data class GameStakesModel(
    val game: String = "",
    val coefficientForWin: Double = 0.00,
    val coefficientForDraw: Double = 0.00,
    val coefficientForDefeat: Double = 0.00,
    val coefficientForFine: Double = 0.00,
    val stakes: List<StakeModel>
)