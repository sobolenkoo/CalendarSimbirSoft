package com.example.calendarsimbirsoft

import android.app.Application
import com.example.calendarsimbirsoft.data.EventsApi
import com.example.calendarsimbirsoft.data.EventsDTOMapper
import com.example.calendarsimbirsoft.data.EventsMockRepository
import com.example.calendarsimbirsoft.data.EventsRepository
import com.example.calendarsimbirsoft.presentation.EventsMapper
import com.example.calendarsimbirsoft.presentation.viewModel.EventsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        val appModule = module {
            single<EventsRepository> { EventsMockRepository(get(), get()) }
            single { EventsDTOMapper() }
            single { EventsMapper() }
            viewModel { EventsViewModel(get(), get()) }
            single { EventsApi(applicationContext) }
        }

        startKoin {
            modules(appModule)
        }
    }
}
