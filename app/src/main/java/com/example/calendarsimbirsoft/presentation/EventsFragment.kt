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
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventsFragment : Fragment(R.layout.events_fragment) {
    private val binding: EventsFragmentBinding by viewBinding()
    private val adapterDelegate = MainAdapter { eventsUI -> moveToDetailsFragment(eventsUI) }
    private val viewModel: EventsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        binding.recyclerView.adapter = adapterDelegate
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentEvents.collect { events ->
                    adapterDelegate.items = events
                }
            }
        }
    }

    private fun moveToDetailsFragment(eventsUI: EventsUI) {
        findNavController().navigate(
            EventsFragmentDirections.actionEventsFragmentToEventsDetailsFragment(
                eventsUI
            )
        )
    }
}
