package com.example.calendarsimbirsoft.presentation

sealed interface Navigation {
    data class EventsDetails(val eventsUI: EventsUI) : Navigation
    data object CreateEvent: Navigation
    data object Pop : Navigation
}