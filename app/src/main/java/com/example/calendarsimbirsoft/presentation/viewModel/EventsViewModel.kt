package com.example.calendarsimbirsoft.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarsimbirsoft.data.EventsRepository
import com.example.calendarsimbirsoft.presentation.EmptyCellItem
import com.example.calendarsimbirsoft.presentation.EventsUI
import com.example.calendarsimbirsoft.presentation.Navigation
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
import javax.inject.Inject

class EventsViewModel @Inject constructor(
    private val eventsLocalRepository: EventsRepository
) : ViewModel() {

    private val _currentCells = MutableStateFlow<List<ListItem>>(emptyList())
    val currentCells: StateFlow<List<ListItem>> = _currentCells.asStateFlow()

    private val _currentEvent: MutableStateFlow<EventsUI?> = MutableStateFlow(null)

    private val _currentDate = MutableStateFlow<Long>(System.currentTimeMillis())
    val currentDate: StateFlow<Long> = _currentDate.asStateFlow()

    private val _navigationItem = MutableSharedFlow<Navigation>()
    val navigationItem: SharedFlow<Navigation> = _navigationItem.asSharedFlow()

    private val dateTimeFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd MMMM yyyy г.", Locale("ru"))

    init {
        val date = Instant.fromEpochMilliseconds(System.currentTimeMillis()).toLocalDateTime(
            TimeZone.UTC
        ).toJavaLocalDateTime().format(dateTimeFormatter)
        filteringCellsFromDataBaseByDate(date)
    }

    private fun filteringCellsFromDataBaseByDate(date: String) {
        _currentCells.value = createEmptyCells()
        viewModelScope.launch {
            eventsLocalRepository.readEventsByDate(date).filter { entity ->
                entity.date == date
            }.forEach {
                setEvent(it)
            }
        }
    }

    fun setEvent(eventsUI: EventsUI) {
        viewModelScope.launch {
            val setDate = Instant.fromEpochMilliseconds(_currentDate.value).toLocalDateTime(
                TimeZone.UTC
            ).toJavaLocalDateTime().format(dateTimeFormatter)
            eventsUI.let {
                val currentEvents = _currentCells.value
                val changedEvents = currentEvents.map { event ->
                    if ((eventsUI.startTime >= event.startTime && eventsUI.startTime < event.endTime) ||
                        (eventsUI.endTime > event.startTime && eventsUI.endTime <= event.endTime) ||
                        (eventsUI.startTime < event.startTime && eventsUI.endTime > event.endTime && eventsUI.date == setDate && eventsUI.id == event.itemId)
                    ) {
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
            val setDate = Instant.fromEpochMilliseconds(date).toLocalDateTime(
                TimeZone.UTC
            ).toJavaLocalDateTime().format(dateTimeFormatter)
            filteringCellsFromDataBaseByDate(setDate)
        }
    }

    fun onEventClick(eventsUI: EventsUI) {
        viewModelScope.launch {
            _navigationItem.emit(Navigation.EventsDetails(eventsUI))
            _currentEvent.value = eventsUI
        }
    }

    fun onCreateEventClick() {
        viewModelScope.launch {
            _navigationItem.emit(Navigation.CreateEvent)
        }
    }

    fun onBackStackClick() {
        viewModelScope.launch {
            _navigationItem.emit(Navigation.Pop)
        }
    }

    fun onBtnDeleteClick() {
        viewModelScope.launch {
            val currentEvent = _currentEvent.value
            currentEvent?.let {
                eventsLocalRepository.deleteEvent(currentEvent)
                _navigationItem.emit(Navigation.Pop)
                filteringCellsFromDataBaseByDate(currentEvent.date)
            }
        }
    }

    fun onDetailsBtnConfirmClicked() {
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
            filteringCellsFromDataBaseByDate(setDate)
            _navigationItem.emit(Navigation.Pop)
        }
    }

    fun updateTitle(title: String) {
        _currentEvent.value = _currentEvent.value?.copy(name = title)
    }

    fun updateDescription(description: String) {
        _currentEvent.value = _currentEvent.value?.copy(description = description)
    }

    fun updateDate(date: String) {
        _currentEvent.value = _currentEvent.value?.copy(date = date)
    }


    fun updateStartTime(startTime: String) {
        _currentEvent.value = _currentEvent.value?.copy(startTime = startTime)
    }

    fun updateEndTime(endTime: String) {
        _currentEvent.value = _currentEvent.value?.copy(endTime = endTime)
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
