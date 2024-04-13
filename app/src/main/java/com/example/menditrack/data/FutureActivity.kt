package com.example.menditrack.data

import java.time.LocalDateTime

data class FutureActivity(
    val time: LocalDateTime,
    val title: String,
    val body: String
)
