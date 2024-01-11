package com.example.calendarsimbirsoft.presentation

import android.os.Parcelable
import com.example.calendarsimbirsoft.presentation.recycler.ListItem
import kotlinx.android.parcel.Parcelize

data class EventsUiDTO(
    var events: List<EventsUI>
)

@Parcelize
data class EventsUI(
    val id: String,
    val dateStart: String,
    val dateFinish: String,
    val name: String,
    val description: String,
) : ListItem, Parcelable {
    override val itemId: String = id
}
