package com.example.calendarsimbirsoft.data

import com.example.calendarsimbirsoft.data.room.EventEntity
import com.example.calendarsimbirsoft.data.room.EventsDao
import com.example.calendarsimbirsoft.presentation.EventsUI

class EventsLocalRepositoryImpl(
    private val eventsDao: EventsDao
) : EventsLocalRepository {


    override suspend fun readEventsByDate(date: String): List<EventsUI> {
        return eventsDao.readAllEvents().filter { entity ->
            entity.startDate == date
        }.map { entity ->
            EventsUI(
                id = entity.id,
                startDate = entity.startDate,
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
            startDate = event.startDate,
            name = event.name,
            description = event.description,
            startTime = event.startTime,
            endTime = event.endTime
        )

        eventsDao.addEvents(event = entities)
    }

    override suspend fun deleteEvents(event: EventsUI) {

        val entities = EventEntity(
            id = event.id,
            startDate = event.startDate,
            name = event.name,
            description = event.description,
            startTime = event.startTime,
            endTime = event.endTime
        )

        eventsDao.deleteEvent(event = entities)
    }
}

interface EventsLocalRepository {
    suspend fun readEventsByDate(date: String): List<EventsUI>

    suspend fun updateEvent(event: EventsUI)
    suspend fun deleteEvents(event: EventsUI)

}