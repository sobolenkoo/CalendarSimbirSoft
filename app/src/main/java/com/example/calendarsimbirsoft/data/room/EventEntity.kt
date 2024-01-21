package com.example.calendarsimbirsoft.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey val id: String,
    val startTime: String,
    val endTime: String,
    val name: String,
    val description: String,
    val date: String
)