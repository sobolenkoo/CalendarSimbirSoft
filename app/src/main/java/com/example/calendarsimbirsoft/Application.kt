package com.example.calendarsimbirsoft

import android.app.Application
import com.example.calendarsimbirsoft.dagger.DI

class Application : Application() {
    override fun onCreate() {
        DI.setupDi(this)
        super.onCreate()
    }
}
