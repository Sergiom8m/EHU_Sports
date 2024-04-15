package com.example.menditrack.data

import java.time.LocalDateTime

// Data class for modeling the scheduled future activities
data class FutureActivity(
    val time: LocalDateTime,
    val title: String,
    val body: String
)