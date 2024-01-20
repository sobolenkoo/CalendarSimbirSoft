package com.example.calendarsimbirsoft

import android.app.Application
import com.example.calendarsimbirsoft.data.EventsLocalRepository
import com.example.calendarsimbirsoft.data.EventsLocalRepositoryImpl
import com.example.calendarsimbirsoft.data.room.EventsDataBase
import com.example.calendarsimbirsoft.presentation.viewModel.EventsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        val appModule = module {

            single { EventsLocalRepositoryImpl( get()) }

            single<EventsLocalRepository> { EventsLocalRepositoryImpl(get()) }
            single { EventsDataBase.getDataBase(applicationContext).eventsDao() }
            viewModel { EventsViewModel(get()) }
        }

        startKoin {
            modules(appModule)
        }
    }
}
