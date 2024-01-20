package com.example.calendarsimbirsoft.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarsimbirsoft.data.EventsLocalRepository
import com.example.calendarsimbirsoft.presentation.EmptyCellItem
import com.example.calendarsimbirsoft.presentation.EventsUI
import com.example.calendarsimbirsoft.presentation.recycler.ListItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


sealed interface Navigation {
    data class EventsDetails(val eventsUI: EventsUI) : Navigation
    data class EmptyEvents(val emptyItem: EmptyCellItem) : Navigation
    data object Pop : Navigation
}

class EventsViewModel(
    private val eventsLocalRepository: EventsLocalRepository
) : ViewModel() {

    private val _currentCells = MutableStateFlow<List<ListItem>>(emptyList())
    val currentCells: StateFlow<List<ListItem>> = _currentCells.asStateFlow()


    private val _currentDate = MutableStateFlow<Long>(System.currentTimeMillis())
    val currentDate: StateFlow<Long> = _currentDate.asStateFlow()

    private val _navigationItem = MutableSharedFlow<Navigation>()
    val navigationItem: SharedFlow<Navigation> = _navigationItem.asSharedFlow()

    private val _currentEvent: MutableStateFlow<EventsUI?> = MutableStateFlow(null)


    init {
        val dateTimeFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd MMMM yyyy г.", Locale("ru"))
        val date = Instant.fromEpochMilliseconds(System.currentTimeMillis()).toLocalDateTime(
            TimeZone.UTC
        ).toJavaLocalDateTime().format(dateTimeFormatter)
        filteringEventsFromDataBase(date)
    }

    fun filteringEventsFromDataBase(date: String) {
        _currentCells.value = createEmptyCells()
        viewModelScope.launch {
            eventsLocalRepository.readEventsByDate(date).filter { entity ->
                entity.startDate == date
            }.forEach {
                setEvent(it)
            }
        }
    }

    fun setEvent(eventsUI: EventsUI) {
        viewModelScope.launch {
            val dateTimeFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("dd MMMM yyyy г.", Locale("ru"))
            val setDate = Instant.fromEpochMilliseconds(_currentDate.value).toLocalDateTime(
                TimeZone.UTC
            ).toJavaLocalDateTime().format(dateTimeFormatter)
            eventsUI.let {
                val currentEvents = _currentCells.value
                val changedEvents = currentEvents.map { event ->
                    if (eventsUI.startTime >= event.startTime && eventsUI.endTime <= event.endTime && eventsUI.startDate == setDate) {
                        eventsUI
                    } else {
                        event
                    }
                }

                _currentCells.value = changedEvents

            }
            eventsLocalRepository.updateEvent(eventsUI)
        }
    }

    fun setCurrentDate(date: Long) {
        _currentDate.value = date
        _currentCells.value = createEmptyCells()
        viewModelScope.launch {
            val dateTimeFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("dd MMMM yyyy г.", Locale("ru"))
            val setDate = Instant.fromEpochMilliseconds(date).toLocalDateTime(
                TimeZone.UTC
            ).toJavaLocalDateTime().format(dateTimeFormatter)
            val eventsDB = eventsLocalRepository.readEventsByDate(setDate)
            eventsDB.forEach {
                setEvent(it)
            }
        }
    }

    fun onEventClick(eventsUI: EventsUI) {
        viewModelScope.launch {
            _navigationItem.emit(Navigation.EventsDetails(eventsUI))
            _currentEvent.value = eventsUI
        }
    }

    fun onBtnDeleteClick() {
        viewModelScope.launch {
            val currentEvent = _currentEvent.value
            currentEvent?.let {
                eventsLocalRepository.deleteEvents(currentEvent)
                _navigationItem.emit(Navigation.Pop)
                filteringEventsFromDataBase(currentEvent.startDate)
            }

        }
    }

    fun onBtnConfirmClicked() {
        val dateTimeFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd MMMM yyyy г.", Locale("ru"))
        val setDate = Instant.fromEpochMilliseconds(_currentDate.value).toLocalDateTime(
            TimeZone.UTC
        ).toJavaLocalDateTime().format(dateTimeFormatter)
        viewModelScope.launch {
            val currentEvent = _currentEvent.value
            currentEvent?.let {
                val currentCells = _currentCells.value
                val changedCells = currentCells.map { event ->
                    if (event.itemId == currentEvent.itemId) {
                        currentEvent
                    } else {
                        event
                    }
                }

                eventsLocalRepository.updateEvent(currentEvent)

                _currentEvent.value = null
                _currentCells.value = changedCells
            }
            filteringEventsFromDataBase(setDate)
            _navigationItem.emit(Navigation.Pop)
        }
    }

    fun updateTitleText(text: String) {
        _currentEvent.value = _currentEvent.value?.copy(name = text)
    }

    fun updateDescriptionText(text: String) {
        _currentEvent.value = _currentEvent.value?.copy(description = text)
    }

    fun updateDateStartText(dateStart: String) {
        _currentEvent.value = _currentEvent.value?.copy(startDate = dateStart)
    }


    fun updateTimeStartText(timeStart: String) {
        _currentEvent.value = _currentEvent.value?.copy(startTime = timeStart)
    }

    fun updateTimeFinishText(timeFinish: String) {
        _currentEvent.value = _currentEvent.value?.copy(endTime = timeFinish)
    }


    private fun createEmptyCells() = listOf(
        EmptyCellItem("1", "00:00", "01:00"),
        EmptyCellItem("2", "01:00", "02:00"),
        EmptyCellItem("3", "02:00", "03:00"),
        EmptyCellItem("4", "03:00", "04:00"),
        EmptyCellItem("5", "04:00", "05:00"),
        EmptyCellItem("6", "05:00", "06:00"),
        EmptyCellItem("7", "06:00", "07:00"),
        EmptyCellItem("8", "07:00", "08:00"),
        EmptyCellItem("9", "08:00", "09:00"),
        EmptyCellItem("10", "09:00", "10:00"),
        EmptyCellItem("11", "10:00", "11:00"),
        EmptyCellItem("12", "11:00", "12:00"),
        EmptyCellItem("13", "12:00", "13:00"),
        EmptyCellItem("14", "13:00", "14:00"),
        EmptyCellItem("15", "14:00", "15:00"),
        EmptyCellItem("16", "15:00", "16:00"),
        EmptyCellItem("17", "16:00", "17:00"),
        EmptyCellItem("18", "17:00", "18:00"),
        EmptyCellItem("19", "18:00", "19:00"),
        EmptyCellItem("20", "19:00", "20:00"),
        EmptyCellItem("21", "20:00", "21:00"),
        EmptyCellItem("22", "21:00", "22:00"),
        EmptyCellItem("23", "22:00", "23:00"),
        EmptyCellItem("24", "23:00", "24:00"),
    )
}
