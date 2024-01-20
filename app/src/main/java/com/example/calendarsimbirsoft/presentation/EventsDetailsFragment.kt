package com.example.calendarsimbirsoft.presentation

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
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
import com.example.calendarsimbirsoft.databinding.EventsDetailsFragmentBinding
import com.example.calendarsimbirsoft.presentation.viewModel.EventsViewModel
import com.example.calendarsimbirsoft.presentation.viewModel.Navigation
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class EventsDetailsFragment : Fragment(R.layout.events_details_fragment) {
    private val binding: EventsDetailsFragmentBinding by viewBinding()
    private val arguments: EventsDetailsFragmentArgs by navArgs()
    private val viewModel: EventsViewModel by sharedViewModel()
    private val calendar: Calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        initViews()
        bindDate()
        bindTime()
    }

    private fun bindDate() {
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy г.", Locale("ru"))
        binding.startDate.setText(arguments.eventsUI.startDate)

        val dateStartSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.startDate.setText(dateFormatter.format(calendar.time))
            }

        binding.startDate.setOnClickListener {
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
        binding.timeStart.setText(
            arguments.eventsUI.startTime
        )

//        val calendar = Calendar.getInstance()
//        calendar.add(Calendar.HOUR_OF_DAY, 1)
//        val formattedTimeFinish = SimpleDateFormat("HH:mm", Locale("ru")).format(arguments.eventsUI.endTime)
        binding.timeFinish.setText(arguments.eventsUI.endTime)


        val maxTimeFinishCalendar = Calendar.getInstance()

        val updateTimeFields: (Calendar, Calendar) -> Unit = { startCalendar, endCalendar ->
            binding.timeStart.setText(startCalendar.formatTime("HH:mm"))
            binding.timeFinish.setText(endCalendar.formatTime("HH:mm"))
        }

        val timeStartSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            maxTimeFinishCalendar.time = calendar.time
            maxTimeFinishCalendar.add(Calendar.HOUR_OF_DAY, 1)
            maxTimeFinishCalendar.set(Calendar.MINUTE, 0)

            updateTimeFields(calendar, maxTimeFinishCalendar)
        }

        binding.timeStart.setOnClickListener {
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
                binding.timeFinish.setText(maxTimeFinishCalendar.formatTime("HH:mm"))
                showToast("Выбранное время должно быть в промежутке одного часа")
            }

        }

        binding.timeFinish.setOnClickListener {
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

    private fun initViews() {
        val eventUI = arguments.eventsUI
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy г.", Locale("ru"))
        with(binding) {
            title.setText(eventUI.name)
            description.setText(eventUI.description)


        }
    }

    private fun bindViews() {
        with(binding) {
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



            title.doOnTextChanged { titleText, _, _, _ ->
                viewModel.updateTitleText(titleText.toString())
            }


            startDate.doOnTextChanged { dateStart, _, _, _ ->
                viewModel.updateDateStartText(dateStart.toString())
            }


            timeStart.doOnTextChanged { timeStart, _, _, _ ->
                viewModel.updateTimeStartText(timeStart.toString())
            }

            timeFinish.doOnTextChanged { timeFinish, _, _, _ ->
                viewModel.updateTimeFinishText(timeFinish.toString())
            }

            description.doOnTextChanged { descriptionText, _, _, _ ->
                viewModel.updateDescriptionText(descriptionText.toString())
            }


            btnConfirm.setOnClickListener {
                viewModel.onBtnConfirmClicked()
            }

            btnDelete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Подтверждение")
                builder.setMessage("Вы уверены, что хотите удалить это событие?")
                builder.setPositiveButton("Да") { dialog, which ->
                    viewModel.onBtnDeleteClick()
                }
                builder.setNegativeButton("Нет") { dialog, which ->
                    dialog.dismiss()
                }
                builder.create().show()
            }


            btnBackStack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}
