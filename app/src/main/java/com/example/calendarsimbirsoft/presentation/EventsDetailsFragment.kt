package com.example.calendarsimbirsoft.presentation

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calendarsimbirsoft.R
import com.example.calendarsimbirsoft.dagger.DI
import com.example.calendarsimbirsoft.databinding.EventsDetailsFragmentBinding
import com.example.calendarsimbirsoft.presentation.viewModel.EventsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject


class EventsDetailsFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.events_details_fragment
    private val binding: EventsDetailsFragmentBinding by viewBinding()
    private val arguments: EventsDetailsFragmentArgs by navArgs()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by activityViewModels<EventsViewModel> { viewModelFactory }
    private val calendar: Calendar = Calendar.getInstance()

    override fun setUpInjection() = DI.component.inject(this)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        initViews()
        bindDate()
        bindTime()
    }

    private fun initViews() {
        with(binding) {
            addTitle.setText(arguments.eventsUI.name)
            description.setText(arguments.eventsUI.description)
        }
    }

    private fun bindViews() {
        with(binding) {
            viewLifecycleOwner.launchRepeatedly {
                viewModel.navigationItem.onEach { item ->
                    when (item) {
                        is Navigation.EventsDetails -> Unit
                        is Navigation.CreateEvent -> Unit
                        is Navigation.Pop -> findNavController().popBackStack()
                    }
                }.launchIn(this)
            }

            addTitle.doOnTextChanged { titleText, _, _, _ ->
                viewModel.updateTitle(titleText.toString())
            }

            date.doOnTextChanged { date, _, _, _ ->
                viewModel.updateDate(date.toString())
            }

            startTime.doOnTextChanged { startTime, _, _, _ ->
                viewModel.updateStartTime(startTime.toString())
            }

            endTime.doOnTextChanged { endTime, _, _, _ ->
                viewModel.updateEndTime(endTime.toString())
            }

            description.doOnTextChanged { descriptionText, _, _, _ ->
                viewModel.updateDescription(descriptionText.toString())
            }

            btnConfirm.setOnClickListener {
                viewModel.onDetailsBtnConfirmClicked()
            }

            btnDelete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(getString(R.string.confirmationTitle))
                builder.setMessage(getString(R.string.confirmation))
                builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    viewModel.onBtnDeleteClick()
                }

                builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
                    dialog.dismiss()
                }
                builder.create().show()
            }


            btnBackStack.setOnClickListener {
                viewModel.onBackStackClick()
            }
        }
    }

    private fun bindDate() {
        with(binding) {
            val dateFormatter = SimpleDateFormat("dd MMMM yyyy г.", Locale("ru"))
            date.setText(arguments.eventsUI.date)

            val dateStartSetListener =
                DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    date.setText(dateFormatter.format(calendar.time))
                }

            date.setOnClickListener {
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
    }

    private fun bindTime() {
        with(binding) {
            startTime.setText(arguments.eventsUI.startTime)
            endTime.setText(arguments.eventsUI.endTime)

            val maxTimeFinishCalendar = Calendar.getInstance()
            val updateTimeFields: (Calendar, Calendar) -> Unit = { startCalendar, endCalendar ->
                startTime.setText(startCalendar.formatTime("HH:mm"))
                endTime.setText(endCalendar.formatTime("HH:mm"))
            }

            val timeStartSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)

                maxTimeFinishCalendar.time = calendar.time
                maxTimeFinishCalendar.add(Calendar.HOUR_OF_DAY, 1)
                maxTimeFinishCalendar.set(Calendar.MINUTE, 0)

                updateTimeFields(calendar, maxTimeFinishCalendar)
            }

            startTime.setOnClickListener {
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
                    endTime.setText(maxTimeFinishCalendar.formatTime("HH:mm"))
                    showToast(getString(R.string.errorTimeInvalid))
                } else {

                    endTime.setText(newTimeFinish.formatTime("HH:mm"))
                }
            }
            endTime.setOnClickListener {
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
    }

    private fun Calendar.formatTime(pattern: String): String =
        SimpleDateFormat(pattern, Locale("ru")).format(time)

    private fun showToast(message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
