package com.example.calendarsimbirsoft.data

import com.example.calendarsimbirsoft.domain.Events
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class EventsDTOMapper {
    fun mapEventDTOtoEvents(eventDTO: EventDTO): Events {
        return Events(
            id = eventDTO.id,
            dateStart = Instant.fromEpochSeconds(eventDTO.dateStart).toLocalDateTime(TimeZone.UTC),
            dateFinish = Instant.fromEpochSeconds(eventDTO.dateFinish).toLocalDateTime(TimeZone.UTC),
            name = eventDTO.name,
            description = eventDTO.description
        )
    }
}
