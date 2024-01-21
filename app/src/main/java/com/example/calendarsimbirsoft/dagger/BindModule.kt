package com.example.calendarsimbirsoft.dagger

import com.example.calendarsimbirsoft.data.EventsLocalRepository
import com.example.calendarsimbirsoft.data.EventsRepository
import dagger.Binds
import dagger.Module

@Module
abstract class BindModule {
    @Binds
    abstract fun bindEventsLocalRepository(bind: EventsLocalRepository): EventsRepository
}