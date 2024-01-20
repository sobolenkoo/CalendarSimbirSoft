package com.example.calendarsimbirsoft.presentation

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calendarsimbirsoft.R
import com.example.calendarsimbirsoft.databinding.CreateEventsFragmentBinding
import com.example.calendarsimbirsoft.presentation.viewModel.EventsViewModel
import com.example.calendarsimbirsoft.presentation.viewModel.Navigation
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateEventsFragment : Fragment(R.layout.create_events_fragment) {
    private val binding: CreateEventsFragmentBinding by viewBinding()
    private val viewModel: EventsViewModel by sharedViewModel()
    private val calendar: Calendar = Calendar.getInstance()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindDate()
        bindTime()
        bindViews()
    }

    private fun bindDate() {
        binding.createDateStart.setText(
            SimpleDateFormat("dd MMMM yyyy г.", Locale("ru")).format(
                System.currentTimeMillis()
            )
        )


        val dateFormatter = SimpleDateFormat("dd MMMM yyyy г.", Locale("ru"))

        val dateStartSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.createDateStart.setText(dateFormatter.format(calendar.time))
            }

        binding.createDateStart.setOnClickListener {
            context?.let {
                DatePickerDialog(
                    it, dateStartSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }

    }

    fun bindTime() {
        binding.createTimeStart.setText(
            SimpleDateFormat(
                "HH:00",
                Locale("ru")
            ).format(calendar.time)
        )

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        val formattedTimeFinish = SimpleDateFormat("HH:00", Locale("ru")).format(calendar.time)
        binding.createTimeFinish.setText(formattedTimeFinish)

        val maxTimeFinishCalendar = Calendar.getInstance()

        val updateTimeFields: (Calendar, Calendar) -> Unit = { startCalendar, endCalendar ->
            binding.createTimeStart.setText(startCalendar.formatTime("HH:mm"))
            binding.createTimeFinish.setText(endCalendar.formatTime("HH:mm"))
        }

        val timeStartSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            maxTimeFinishCalendar.time = calendar.time
            maxTimeFinishCalendar.add(Calendar.HOUR_OF_DAY, 1)
            maxTimeFinishCalendar.set(Calendar.MINUTE, 0)

            updateTimeFields(calendar, maxTimeFinishCalendar)
        }

        binding.createTimeStart.setOnClickListener {
            TimePickerDialog(
                context,
                AlertDialog.THEME_HOLO_LIGHT,
                timeStartSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        val timeFinishSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            val newTimeFinish = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }

            if (newTimeFinish !in calendar..maxTimeFinishCalendar) {
                binding.createTimeFinish.setText(maxTimeFinishCalendar.formatTime("HH:mm"))
                showToast("Выбранное время должно быть в промежутке одного часа")
            }

        }

        binding.createTimeFinish.setOnClickListener {
            TimePickerDialog(
                context,
                AlertDialog.THEME_HOLO_LIGHT,
                timeFinishSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun Calendar.formatTime(pattern: String): String =
        SimpleDateFormat(pattern, Locale("ru")).format(time)

    private fun showToast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    private fun bindViews() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationItem.collect { item ->
                    when (item) {
                        is Navigation.EventsDetails -> Unit
                        is Navigation.EmptyEvents -> Unit
                        is Navigation.Pop -> findNavController().popBackStack()
                    }
                }
            }
        }

        with(binding) {
            val eventsUI = EventsUI(
                id = "",
                startTime = createTimeStart.text.toString(),
                endTime = createTimeFinish.text.toString(),
                name = createTitle.text.toString(),
                description = createDescription.text.toString(),
                startDate = createDateStart.text.toString()

            )


            createTitle.doOnTextChanged { titleText, _, _, _ ->
                eventsUI.name = titleText.toString()
            }

            createDateStart.doOnTextChanged { dateStart, _, _, _ ->
                eventsUI.startDate = dateStart.toString()
            }


            createTimeStart.doOnTextChanged { timeStart, _, _, _ ->
                eventsUI.startTime = timeStart.toString()
            }

            createTimeFinish.doOnTextChanged { timeFinish, _, _, _ ->
                eventsUI.endTime = timeFinish.toString()
            }

            createDescription.doOnTextChanged { descriptionText, _, _, _ ->
                eventsUI.description = descriptionText.toString()
            }

            binding.btnConfirm.setOnClickListener {
                eventsUI.id = calendar.timeInMillis.toString()
                if (isEventsUIValid(eventsUI)) {
                    viewModel.setEvent(eventsUI)
                    findNavController().popBackStack()
                } else {
                    showToast("Заполните информацию о событии")
                }
            }

            binding.btnBackStack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun isEventsUIValid(eventsUI: EventsUI): Boolean {
        return eventsUI.startTime.isNotBlank() &&
                eventsUI.endTime.isNotBlank() &&
                eventsUI.name.isNotBlank() &&
                eventsUI.description.isNotBlank() &&
                eventsUI.startDate.isNotBlank()

    }
}



