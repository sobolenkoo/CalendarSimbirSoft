package com.example.calendarsimbirsoft.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun LifecycleOwner.launchRepeatedly(block: suspend CoroutineScope.() -> Unit): Job =
    lifecycleScope.launch { repeatOnLifecycle(Lifecycle.State.STARTED, block) }