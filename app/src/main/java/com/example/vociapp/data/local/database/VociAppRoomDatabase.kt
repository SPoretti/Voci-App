package com.example.vociapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vociapp.data.local.dao.HomelessDao
import com.example.vociapp.data.local.dao.RequestDao
import com.example.vociapp.data.local.dao.SyncQueueDao
import com.example.vociapp.data.local.dao.UpdateDao
import com.example.vociapp.data.local.dao.VolunteerDao

@Database(
    entities = [Homeless::class, Volunteer::class, Request::class, Update::class, SyncAction::class],
    version = 3,
    exportSchema = false // Set to true for schema versioning; useful for migrations
)
@TypeConverters(Converters::class)
abstract class VociAppRoomDatabase : RoomDatabase() {

    // Abstract methods to get DAOs
    abstract fun homelessDao(): HomelessDao
    abstract fun volunteerDao(): VolunteerDao
    abstract fun requestDao(): RequestDao
    abstract fun updateDao(): UpdateDao
    abstract fun syncQueueDao(): SyncQueueDao

    companion object {
        @Volatile
        private var INSTANCE: VociAppRoomDatabase? = null

        fun getDatabase(context: Context): VociAppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VociAppRoomDatabase::class.java,
                    "voci_app_database" // Name of the database file
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


