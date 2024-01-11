package com.example.calendarsimbirsoft.data

import com.example.calendarsimbirsoft.domain.Events
import com.example.calendarsimbirsoft.domain.NetworkResults

class EventsMockRepository(
    private val api: EventsApi,
    private val mapper: EventsDTOMapper
) : EventsRepository {
    override suspend fun getEventsData(): NetworkResults<List<Events>> {
        val resultsFromJson = api.getEventsResultsFromJson()
        if (resultsFromJson.isSuccessful) {
            val body = resultsFromJson.body()
            if (body != null) {
                return NetworkResults.Success(body.map { mapper.mapEventDTOtoEvents(it) })
            }
        }
        return NetworkResults.Error("Ошибка соединения с сервером")
    }
}
