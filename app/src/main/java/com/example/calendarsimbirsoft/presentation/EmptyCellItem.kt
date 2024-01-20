package com.example.calendarsimbirsoft.presentation

import android.os.Parcelable
import com.example.calendarsimbirsoft.presentation.recycler.ListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmptyCellItem(
    val id: String,
    override val startTime: String,
    override val endTime: String
) : ListItem, Parcelable {
    override val itemId: String = id
}