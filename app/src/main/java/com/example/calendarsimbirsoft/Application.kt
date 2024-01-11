package com.example.calendarsimbirsoft

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        val appModule = module {

        }

        startKoin{
            modules(appModule)
        }
    }
}
