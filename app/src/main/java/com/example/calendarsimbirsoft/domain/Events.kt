package com.example.calendarsimbirsoft.domain

import kotlinx.datetime.LocalDateTime

data class Events(
    val id: String,
    val dateStart: LocalDateTime,
    val dateFinish: LocalDateTime,
    val name: String,
    val description: String,
)