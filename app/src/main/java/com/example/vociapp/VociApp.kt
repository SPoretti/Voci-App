package com.example.vociapp

import android.app.Application
import android.content.Context
import androidx.work.WorkManager
import com.example.vociapp.data.local.database.VociAppRoomDatabase
import com.example.vociapp.di.ServiceLocator
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VociApp : Application(){
    companion object {
        lateinit var serviceLocator: ServiceLocator
            private set
    }

    override fun onCreate() {
        super.onCreate()

        //clearDatabaseAndSyncQueue(context = applicationContext)
        ServiceLocator.initialize(this, FirebaseFirestore.getInstance())
    }

    //for debugging purposes
    private fun clearDatabaseAndSyncQueue(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = VociAppRoomDatabase.getDatabase(context)
            db.clearAllTables()
            WorkManager.getInstance(context).cancelAllWork()
        }
    }
}