package com.example.calendarsimbirsoft.presentation

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calendarsimbirsoft.R
import com.example.calendarsimbirsoft.dagger.DI
import com.example.calendarsimbirsoft.databinding.CreateEventsFragmentBinding
import com.example.calendarsimbirsoft.presentation.viewModel.EventsViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class CreateEventsFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.create_events_fragment
    private val binding: CreateEventsFragmentBinding by viewBinding()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by activityViewModels<EventsViewModel> { viewModelFactory }
    private val calendar: Calendar = Calendar.getInstance()

    override fun setUpInjection() = DI.component.inject(this)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindDate()
        bindTime()
        bindViews()
    }

    private fun bindDate() {
        binding.createDate.setText(
            SimpleDateFormat("dd MMMM yyyy г.", Locale("ru")).format(System.currentTimeMillis()))

        val dateFormatter = SimpleDateFormat("dd MMMM yyyy г.", Locale("ru"))

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.createDate.setText(dateFormatter.format(calendar.time))
            }

        binding.createDate.setOnClickListener {
            context?.let {
                DatePickerDialog(
                    it, dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    private fun bindTime() {
        binding.createStartTime.setText(
            SimpleDateFormat(
                "HH:00",
                Locale("ru")
            ).format(calendar.time)
        )

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        val formattedTimeFinish = SimpleDateFormat("HH:00", Locale("ru")).format(calendar.time)
        binding.createEndTime.setText(formattedTimeFinish)

        val maxTimeFinishCalendar = Calendar.getInstance()

        val updateTimeFields: (Calendar, Calendar) -> Unit = { startCalendar, endCalendar ->
            binding.createStartTime.setText(startCalendar.formatTime("HH:mm"))
            binding.createEndTime.setText(endCalendar.formatTime("HH:mm"))
        }

        val timeStartSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            maxTimeFinishCalendar.time = calendar.time
            maxTimeFinishCalendar.add(Calendar.HOUR_OF_DAY, 1)
            maxTimeFinishCalendar.set(Calendar.MINUTE, 0)

            updateTimeFields(calendar, maxTimeFinishCalendar)
        }

        binding.createStartTime.setOnClickListener {
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
                binding.createEndTime.setText(maxTimeFinishCalendar.formatTime("HH:mm"))
                showToast(getString(R.string.errorTimeInvalid))
            } else {

                binding.createEndTime.setText(newTimeFinish.formatTime("HH:mm"))
            }
        }

        binding.createEndTime.setOnClickListener {
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

    private fun bindViews() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationItem.collect { item ->
                    when (item) {
                        is Navigation.EventsDetails -> Unit
                        is Navigation.CreateEvent -> Unit
                        is Navigation.Pop -> findNavController().popBackStack()
                    }
                }
            }
        }

        with(binding) {
            val eventsUI = EventsUI(
                id = "",
                startTime = createStartTime.text.toString(),
                endTime = createEndTime.text.toString(),
                name = createTitle.text.toString(),
                description = createDescription.text.toString(),
                date = createDate.text.toString()

            )

            createTitle.doOnTextChanged { titleText, _, _, _ ->
                eventsUI.name = titleText.toString()
            }

            createDate.doOnTextChanged { date, _, _, _ ->
                eventsUI.date = date.toString()
            }


            createStartTime.doOnTextChanged { timeStart, _, _, _ ->
                eventsUI.startTime = timeStart.toString()
            }

            createEndTime.doOnTextChanged { timeFinish, _, _, _ ->
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
                    showToast(getString(R.string.errorFillEventInformation))
                }
            }

            binding.btnBackStack.setOnClickListener {
                viewModel.onBackStackClick()
            }
        }
    }

    private fun Calendar.formatTime(pattern: String): String =
        SimpleDateFormat(pattern, Locale("ru")).format(time)

    private fun showToast(message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    private fun isEventsUIValid(eventsUI: EventsUI): Boolean {
        return eventsUI.startTime.isNotBlank() &&
                eventsUI.endTime.isNotBlank() &&
                eventsUI.name.isNotBlank() &&
                eventsUI.description.isNotBlank() &&
                eventsUI.date.isNotBlank()

    }
}
