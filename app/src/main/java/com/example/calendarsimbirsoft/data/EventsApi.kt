package com.example.calendarsimbirsoft.data

import android.content.Context
import com.google.gson.Gson
import retrofit2.Response

class EventsApi(private val context: Context) {

    fun getEventsResultsFromJson(): Response<List<EventDTO>> {
        val readData = readJsonFromAssets("demo/events.json")
        val data = Gson().fromJson(readData, EventsDTO::class.java).events
        return Response.success(data)
    }

    private fun readJsonFromAssets(fileName: String): String =
        context.assets.open(fileName).bufferedReader().use { it.readText() }
}
