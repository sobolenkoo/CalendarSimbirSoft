package com.example.calendarsimbirsoft.presentation.recycler

import com.example.calendarsimbirsoft.presentation.EventsUI
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class MainAdapter(itemClickedListener: (EventsUI) -> Unit) :
    AsyncListDifferDelegationAdapter<ListItem>(EventsDiffUtils()) {
    init {
        delegatesManager
            .addDelegate(MainDelegates.eventsItemsDelegates(itemClickedListener))
    }
}
