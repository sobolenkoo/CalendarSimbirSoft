package com.example.calendarsimbirsoft.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calendarsimbirsoft.R
import com.example.calendarsimbirsoft.databinding.EventsFragmentBinding
import com.example.calendarsimbirsoft.presentation.recycler.MainAdapter

class EventsFragment : Fragment(R.layout.events_fragment) {
    private val binding: EventsFragmentBinding by viewBinding()
    private val adapterDelegate = MainAdapter { eventsUI -> moveToDetailsFragment(eventsUI) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapterDelegate
        adapterDelegate.items =
            listOf(EventsUI("1", "12:00", "13:00", "Task", "description")) //Для проверки адаптера
    }

    private fun moveToDetailsFragment(eventsUI: EventsUI) {
        findNavController().navigate(EventsFragmentDirections.actionEventsFragmentToEventsDetailsFragment(eventsUI))
        
    }
}
