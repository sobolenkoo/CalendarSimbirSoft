package com.example.calendarsimbirsoft.data

import com.example.calendarsimbirsoft.domain.NetworkResults
import com.example.calendarsimbirsoft.presentation.EventsUI

class EventsMockRepository(
    private val api: EventsApi
) : EventsRepository {
    override suspend fun getEventsData(): NetworkResults<List<EventsUI>> {
        val resultsFromJson = api.getEventsResultsFromJson()
        if (resultsFromJson.isSuccessful) {
            val body = resultsFromJson.body()
            if (body != null) {
                return NetworkResults.Success(body.map { it })
            }
        }
        return NetworkResults.Error("Ошибка соединения с сервером")
    }
}
