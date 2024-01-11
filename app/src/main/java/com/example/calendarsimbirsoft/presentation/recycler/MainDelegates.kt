package com.example.calendarsimbirsoft.presentation.recycler

import com.example.calendarsimbirsoft.databinding.EventsItemsBinding
import com.example.calendarsimbirsoft.presentation.EventsUI
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

object MainDelegates {

    fun eventsItemsDelegates(itemClickedListener: (EventsUI) -> Unit) =
        adapterDelegateViewBinding<EventsUI, ListItem, EventsItemsBinding>(
            { inflater, container -> EventsItemsBinding.inflate(inflater, container, false) }
        ) {
            with(binding) {
                root.setOnClickListener { itemClickedListener(item) }
                bind {
                    dateStart.text = item.dateStart
                    dateFinish.text = item.dateFinish
                    titleTask.text = item.name
                }
            }
        }
}
