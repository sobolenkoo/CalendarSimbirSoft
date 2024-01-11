package com.example.calendarsimbirsoft.data

data class EventsDTO(
    var events: List<EventDTO>
)

data class EventDTO(
    val id: String,
    val dateStart: Long,
    val dateFinish: Long,
    val name: String,
    val description: String,
)