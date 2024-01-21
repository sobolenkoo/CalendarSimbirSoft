package com.example.calendarsimbirsoft.presentation.recycler

import com.example.calendarsimbirsoft.databinding.EmptyItemBinding
import com.example.calendarsimbirsoft.databinding.EventsItemsBinding
import com.example.calendarsimbirsoft.presentation.EmptyCellItem
import com.example.calendarsimbirsoft.presentation.EventsUI
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

object MainDelegates {
    fun emptyEventsItemsDelegates() =
        adapterDelegateViewBinding<EmptyCellItem, ListItem, EmptyItemBinding>(
            { inflater, container -> EmptyItemBinding.inflate(inflater, container, false) }
        ) {
            with(binding) {

                bind {

                }
            }
        }

    fun eventsItemsDelegates(itemClickedListener: (EventsUI) -> Unit) =
        adapterDelegateViewBinding<EventsUI, ListItem, EventsItemsBinding>(
            { inflater, container -> EventsItemsBinding.inflate(inflater, container, false) }
        ) {
            with(binding) {
                root.setOnClickListener { itemClickedListener(item) }
                bind {
                    dateStart.text = item.startTime
                    dateFinish.text = item.endTime
                    titleTask.text = item.name
                }
            }
        }
}
