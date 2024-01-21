package com.example.calendarsimbirsoft.dagger

import android.app.Application

object DI {
    lateinit var component: AppComponent

    fun setupDi(application: Application) {
        if (::component.isInitialized) return
        component = DaggerAppComponent
            .builder()
            .application(application)
            .build()
    }
}
