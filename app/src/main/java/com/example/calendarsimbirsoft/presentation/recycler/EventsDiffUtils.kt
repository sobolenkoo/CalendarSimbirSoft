package com.example.calendarsimbirsoft.presentation.recycler

import androidx.recyclerview.widget.DiffUtil

open class EventsDiffUtils : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem.itemId == newItem.itemId

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem.equals(newItem)
}
