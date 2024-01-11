package com.example.calendarsimbirsoft.presentation

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calendarsimbirsoft.R
import com.example.calendarsimbirsoft.databinding.EventsDetailsFragmentBinding
import com.example.calendarsimbirsoft.presentation.viewModel.EventsViewModel
import com.example.calendarsimbirsoft.presentation.viewModel.Navigation
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale


class EventsDetailsFragment : Fragment(R.layout.events_details_fragment) {
    private val binding: EventsDetailsFragmentBinding by viewBinding()
    private val arguments: EventsDetailsFragmentArgs by navArgs()
    private val viewModel: EventsViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        initViews()
    }

    private fun initViews() {
        val eventUI = arguments.eventsUI
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))
        with(binding) {
            title.setText(eventUI.name)
            date.text = Instant.fromEpochMilliseconds(arguments.date).toLocalDateTime(TimeZone.UTC)
                .toJavaLocalDateTime().format(dateTimeFormatter)
            "Начало - ${eventUI.dateStart}".also { dateStart.text = it }
            "Конец - ${eventUI.dateFinish}".also { dateFinish.text = it }
            description.setText(eventUI.description)
        }
    }

    private fun bindViews() {
        with(binding) {
            title.doOnTextChanged { titleText, _, _, _ ->
                viewModel.updateTitleText(titleText.toString())
            }

            description.doOnTextChanged { descriptionText, _, _, _ ->
                viewModel.updateDescriptionText(descriptionText.toString())
            }

            btnConfirm.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.navigationItem.collect { item ->
                            when (item) {
                                is Navigation.EventsDetails -> Unit
                                is Navigation.Pop -> findNavController().popBackStack()
                            }
                        }
                    }
                }
                viewModel.onBtnConfirmClicked()
            }

            btnBackStack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}
