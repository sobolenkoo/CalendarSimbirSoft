package com.example.calendarsimbirsoft.presentation

import android.os.Parcelable
import com.example.calendarsimbirsoft.presentation.recycler.ListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventsUI(
    var id: String,
    override var startTime: String,
    override var endTime: String,
    var name: String,
    var description: String,
    var date: String,
) : ListItem, Parcelable {
    override val itemId: String = id
}
