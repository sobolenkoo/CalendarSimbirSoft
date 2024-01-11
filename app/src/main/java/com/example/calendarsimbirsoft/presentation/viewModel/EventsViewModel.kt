package com.example.calendarsimbirsoft.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarsimbirsoft.data.EventsRepository
import com.example.calendarsimbirsoft.domain.NetworkResults
import com.example.calendarsimbirsoft.presentation.EventsMapper
import com.example.calendarsimbirsoft.presentation.EventsUI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed interface Navigation {
    data class EventsDetails(val eventsUI: EventsUI, val date: Long) : Navigation
    object Pop : Navigation
}

class EventsViewModel(
    private val repository: EventsRepository,
    private val mapper: EventsMapper
) : ViewModel() {
    private var allEvents: List<EventsUI> = emptyList()

    private val _currentEvents = MutableStateFlow<List<EventsUI>>(emptyList())
    val currentEvents: StateFlow<List<EventsUI>> = _currentEvents.asStateFlow()

    private val _currentDate = MutableStateFlow<Long>(System.currentTimeMillis())
    val currentDate: StateFlow<Long> = _currentDate.asStateFlow()

    private val _navigationItem = MutableSharedFlow<Navigation>()
    val navigationItem: SharedFlow<Navigation> = _navigationItem.asSharedFlow()

    private val _currentEvent: MutableStateFlow<EventsUI?> = MutableStateFlow(null)

    init {
        receiveEventsData()
    }

    fun receiveEventsData() {
        _currentEvents.value = emptyList()
        viewModelScope.launch {
            when (val networkResults = repository.getEventsData()) {
                is NetworkResults.Success -> {
                    allEvents = networkResults.result.map { mapper.mapEventsToEventsUI(it) }
                    _currentEvents.value = allEvents
                }

                is NetworkResults.Error -> {}
            }
        }
    }

    fun setCurrentDate(date: Long) {
        _currentDate.value = date
    }

    fun updateTitleText(text: String) {
        _currentEvent.value = _currentEvent.value?.copy(name = text)
    }

    fun updateDescriptionText(text: String) {
        _currentEvent.value = _currentEvent.value?.copy(description = text)
    }

    fun onEventClick(eventsUI: EventsUI) {
        viewModelScope.launch {
            _navigationItem.emit(Navigation.EventsDetails(eventsUI, _currentDate.value))
            _currentEvent.value = eventsUI
        }
    }

    fun onBtnConfirmClicked() {
        viewModelScope.launch {
            _navigationItem.emit(Navigation.Pop)
            val currentEvent = _currentEvent.value
            val currentEvents = _currentEvents.value
            val changedEvents = currentEvents.map { event ->
                if (event.id == currentEvent?.id) {
                    currentEvent
                } else {
                    event
                }
            }
            _currentEvent.value = null
            _currentEvents.value = changedEvents
        }

    }
}
