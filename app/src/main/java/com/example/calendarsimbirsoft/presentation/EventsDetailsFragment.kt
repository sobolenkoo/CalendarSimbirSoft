package com.example.calendarsimbirsoft.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calendarsimbirsoft.R
import com.example.calendarsimbirsoft.databinding.EventsDetailsFragmentBinding
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class EventsDetailsFragment : Fragment(R.layout.events_details_fragment) {
    private val binding: EventsDetailsFragmentBinding by viewBinding()
    private val arguments: EventsDetailsFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        val eventUI = arguments.eventsUI
        with(binding) {
            title.setText(eventUI.name)
            "Начало - ${eventUI.dateStart}".also { dateStart.text = it }
            "Конец - ${eventUI.dateFinish}".also { dateFinish.text = it }
            description.setText(eventUI.description)
        }
    }
}
