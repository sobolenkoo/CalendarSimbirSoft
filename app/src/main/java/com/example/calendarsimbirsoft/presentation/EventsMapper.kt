package com.example.calendarsimbirsoft.presentation

import com.example.calendarsimbirsoft.domain.Events
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class EventsMapper {
    fun mapEventsToEventsUI(events: Events): EventsUI {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale("ru"))
        val dateStartFormatted = events.dateStart.toJavaLocalDateTime().format(dateTimeFormatter)
        val dateFinishFormatted = events.dateFinish.toJavaLocalDateTime().format(dateTimeFormatter)
        return EventsUI(
            id = events.id,
            dateStart = dateStartFormatted,
            dateFinish = dateFinishFormatted,
            name = events.name,
            description = events.description
        )
    }
}
