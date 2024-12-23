package com.example.vociapp

import android.app.Application
import android.content.Context
import androidx.activity.result.launch
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.vociapp.data.local.database.VociAppRoomDatabase
import com.example.vociapp.data.util.NetworkConnectivityListener
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

//    private lateinit var networkConnectivityListener: NetworkConnectivityListener


    override fun onCreate() {
        super.onCreate()
        ServiceLocator.initialize(this, FirebaseFirestore.getInstance())
        //clearDatabaseAndSyncQueue(context = applicationContext)
//        networkConnectivityListener = NetworkConnectivityListener(applicationContext)
//        networkConnectivityListener.startMonitoring()
        //if (!WorkManager.isInitialized())
            //initializeWorkManager()
    }

    private fun initializeWorkManager() {
        val workManagerConfiguration = Configuration.Builder()
            .build() // No need for a custom WorkerFactory, dependencies will be injected directly
        WorkManager.initialize(this, workManagerConfiguration)
    }

//    override fun onTerminate() {
//        super.onTerminate()
//        networkConnectivityListener.stopMonitoring()
//    }


    fun clearDatabaseAndSyncQueue(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = VociAppRoomDatabase.getDatabase(context)
            db.clearAllTables()
            WorkManager.getInstance(context).cancelAllWork()
        }
    }
}