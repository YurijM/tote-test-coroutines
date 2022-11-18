package com.example.tote_test.models

import java.io.Serializable

data class EmailModel(
    var id: Int = 0,
    var email: String = ""
) : Serializable