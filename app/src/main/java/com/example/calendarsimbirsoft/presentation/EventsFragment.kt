package com.example.calendarsimbirsoft.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calendarsimbirsoft.R
import com.example.calendarsimbirsoft.databinding.EventsFragmentBinding
import com.example.calendarsimbirsoft.presentation.recycler.MainAdapter
import com.example.calendarsimbirsoft.presentation.viewModel.EventsViewModel
import com.example.calendarsimbirsoft.presentation.viewModel.Navigation
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Calendar

class EventsFragment : Fragment(R.layout.events_fragment) {
    private val binding: EventsFragmentBinding by viewBinding()
    private val adapterDelegate = MainAdapter{ event -> viewModel.onEventClick(event) }
    private val viewModel: EventsViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }


    private fun bindViews() {
        binding.recyclerView.adapter = adapterDelegate
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentCells.collect { events ->
                    adapterDelegate.items = events
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentDate.collect { date ->
                    binding.calendarView.date = date
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationItem.collect { item ->
                    when (item) {
                        is Navigation.EventsDetails -> moveToDetailsFragment(item)
                        is Navigation.EmptyEvents -> Unit
                        is Navigation.Pop -> findNavController().popBackStack()
                    }
                }
            }
        }
        binding.calendarView.setOnDateChangeListener { _, year: Int, month: Int, dayOfMonth: Int ->
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            viewModel.setCurrentDate(calendar.timeInMillis)

        }
        binding.btnCreateEvent.setOnClickListener {
                moveToCreateEventsFragment()
        }
    }

    private fun moveToDetailsFragment(navigation: Navigation.EventsDetails) {
        findNavController().navigate(
            EventsFragmentDirections.actionEventsFragmentToEventsDetailsFragment(
                navigation.eventsUI
            )
        )
    }

    private fun moveToCreateEventsFragment() {
        findNavController().navigate(
            EventsFragmentDirections.actionEventsFragmentToCreateEventsFragment())
    }

}
