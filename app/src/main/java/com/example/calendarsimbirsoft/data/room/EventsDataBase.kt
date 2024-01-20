package com.example.calendarsimbirsoft.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [EventEntity::class],
    version = 1,
    exportSchema = false
)
abstract class EventsDataBase : RoomDatabase() {
    abstract fun eventsDao(): EventsDao

    companion object {
        fun getDataBase(context: Context): EventsDataBase {
            return Room.databaseBuilder(
                context,
                EventsDataBase::class.java,
                "event"
            ).build()
        }
    }
}
