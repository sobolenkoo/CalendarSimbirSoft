package com.example.calendarsimbirsoft.data

import com.example.calendarsimbirsoft.presentation.EventsUI

interface EventsRepository {
    suspend fun readEventsByDate(date: String): List<EventsUI>

    suspend fun updateEvent(event: EventsUI)

    suspend fun deleteEvent(event: EventsUI)
}