package com.example.calendarsimbirsoft.data

import com.example.calendarsimbirsoft.domain.NetworkResults
import com.example.calendarsimbirsoft.presentation.EventsUI

interface EventsRepository {
    suspend fun getEventsData(): NetworkResults<List<EventsUI>>
}
