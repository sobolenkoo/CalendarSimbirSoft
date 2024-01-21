package com.example.calendarsimbirsoft.dagger

import android.app.Application
import com.example.calendarsimbirsoft.presentation.CreateEventsFragment
import com.example.calendarsimbirsoft.presentation.EventsDetailsFragment
import com.example.calendarsimbirsoft.presentation.EventsFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class, ViewModelModule::class, BindModule::class])
interface AppComponent {
    fun inject(app: Application)
    fun inject(eventsFragment: EventsFragment)
    fun inject(eventsDetailsFragment: EventsDetailsFragment)
    fun inject(createEventsFragment: CreateEventsFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}
