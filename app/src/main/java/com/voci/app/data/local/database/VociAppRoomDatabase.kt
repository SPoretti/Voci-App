package com.voci.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.voci.app.data.local.dao.HomelessDao
import com.voci.app.data.local.dao.PreferenceDao
import com.voci.app.data.local.dao.RequestDao
import com.voci.app.data.local.dao.SyncQueueDao
import com.voci.app.data.local.dao.UpdateDao
import com.voci.app.data.local.dao.VolunteerDao

// Room database instance (singleton)

@Database(
    entities = [Homeless::class, Volunteer::class, Preference::class,Request::class, Update::class, SyncAction::class],
    version = 11,
    exportSchema = false // Set to true for schema versioning; useful for migrations
)
@TypeConverters(Converters::class)//uses Converters class as typeConverter
abstract class VociAppRoomDatabase : RoomDatabase() {

    // Abstract methods to get DAOs
    abstract fun homelessDao(): HomelessDao
    abstract fun volunteerDao(): VolunteerDao
    abstract fun PreferenceDao(): PreferenceDao
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


