package com.example.calendarsimbirsoft.data

import com.example.calendarsimbirsoft.domain.Events
import com.example.calendarsimbirsoft.domain.NetworkResults

interface EventsRepository {
    suspend fun getEventsData(): NetworkResults<List<Events>>
}
