package com.example.calendarsimbirsoft.data

import com.example.calendarsimbirsoft.data.room.EventEntity
import com.example.calendarsimbirsoft.data.room.EventsDao
import com.example.calendarsimbirsoft.presentation.EventsUI
import javax.inject.Inject

class EventsLocalRepository @Inject constructor(
    private val eventsDao: EventsDao
) : EventsRepository {

    override suspend fun readEventsByDate(date: String): List<EventsUI> {
        return eventsDao.readAllEvents().filter { entity ->
            entity.date == date
        }.map { entity ->
            EventsUI(
                id = entity.id,
                date = entity.date,
                name = entity.name,
                description = entity.description,
                startTime = entity.startTime,
                endTime = entity.endTime
            )
        }
    }

    override suspend fun updateEvent(event: EventsUI) {
        val entities = EventEntity(
            id = event.id,
            date = event.date,
            name = event.name,
            description = event.description,
            startTime = event.startTime,
            endTime = event.endTime
        )

        eventsDao.updateEvent(event = entities)
    }

    override suspend fun deleteEvent(event: EventsUI) {

        val entities = EventEntity(
            id = event.id,
            date = event.date,
            name = event.name,
            description = event.description,
            startTime = event.startTime,
            endTime = event.endTime
        )

        eventsDao.deleteEvent(event = entities)
    }
}

