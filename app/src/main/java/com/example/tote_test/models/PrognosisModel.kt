package com.example.tote_test.models

import java.io.Serializable

data class PrognosisModel(
    val gameId: Int = 0,
    val game: String = "",
    val coefficientForWin: Double = 0.00,
    val coefficientForDraw: Double = 0.00,
    val coefficientForDefeat: Double = 0.00,
    val coefficientForFine: Double = 0.00
) : Serializable