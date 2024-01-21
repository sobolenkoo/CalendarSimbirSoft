package com.example.calendarsimbirsoft.dagger

import android.app.Application
import android.content.Context
import com.example.calendarsimbirsoft.data.room.EventsDao
import com.example.calendarsimbirsoft.data.room.EventsDataBase
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideEventsDataBase(context: Context): EventsDao {
        return EventsDataBase.getDataBase(context).eventsDao()
    }

    @Provides
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }
}