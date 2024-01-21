package com.example.calendarsimbirsoft.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calendarsimbirsoft.R
import com.example.calendarsimbirsoft.dagger.DI
import com.example.calendarsimbirsoft.databinding.EventsFragmentBinding
import com.example.calendarsimbirsoft.presentation.recycler.MainAdapter
import com.example.calendarsimbirsoft.presentation.viewModel.EventsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Calendar
import javax.inject.Inject

class EventsFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.events_fragment
    private val binding: EventsFragmentBinding by viewBinding()
    private val adapterDelegate = MainAdapter { event -> viewModel.onEventClick(event) }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by activityViewModels<EventsViewModel> { viewModelFactory }

    override fun setUpInjection() = DI.component.inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        with(binding) {
            recyclerView.adapter = adapterDelegate
            viewLifecycleOwner.launchRepeatedly {
                viewModel.currentCells.onEach { events ->
                    adapterDelegate.items = events
                }.launchIn(this)
                viewModel.currentDate.onEach { date ->
                    calendarView.date = date
                }.launchIn(this)
                viewModel.navigationItem.onEach { item ->
                    when (item) {
                        is Navigation.EventsDetails -> moveToDetailsFragment(item)
                        is Navigation.CreateEvent -> moveToCreateEventsFragment()
                        is Navigation.Pop -> findNavController().popBackStack()
                    }
                }.launchIn(this)
            }

            calendarView.setOnDateChangeListener { _, year: Int, month: Int, dayOfMonth: Int ->
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                viewModel.setCurrentDate(calendar.timeInMillis)
            }

            btnCreateEvent.setOnClickListener {
                viewModel.onCreateEventClick()
            }
        }
    }

    private fun moveToDetailsFragment(navigation: Navigation.EventsDetails) {
        findNavController().navigate(
            EventsFragmentDirections.actionEventsFragmentToEventsDetailsFragment(navigation.eventsUI)
        )
    }

    private fun moveToCreateEventsFragment() {
        findNavController().navigate(
            EventsFragmentDirections.actionEventsFragmentToCreateEventsFragment()
        )
    }
}
