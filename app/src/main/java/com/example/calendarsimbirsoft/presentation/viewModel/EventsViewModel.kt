package com.example.calendarsimbirsoft.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarsimbirsoft.data.EventsRepository
import com.example.calendarsimbirsoft.domain.NetworkResults
import com.example.calendarsimbirsoft.presentation.EventsUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventsViewModel(
    private val repository: EventsRepository
) : ViewModel() {
    private var allEvents: List<EventsUI> = emptyList()

    private val _currentEvents = MutableStateFlow<List<EventsUI>>(emptyList())
    val currentEvents: StateFlow<List<EventsUI>> = _currentEvents.asStateFlow()

    init {
        receiveEventsData()
    }

    fun receiveEventsData() {
        _currentEvents.value = emptyList()
        viewModelScope.launch {
            when (val networkResults = repository.getEventsData()) {
                is NetworkResults.Success -> {
                    allEvents = networkResults.result
                    _currentEvents.value = allEvents
                }

                is NetworkResults.Error -> {}
            }
        }
    }
}
