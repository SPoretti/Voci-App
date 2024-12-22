package com.example.vociapp

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.vociapp.data.util.NetworkConnectivityListener
import com.example.vociapp.di.ServiceLocator
import com.google.firebase.firestore.FirebaseFirestore

class VociApp : Application(){
    companion object {
        lateinit var serviceLocator: ServiceLocator
            private set
    }

//    private lateinit var networkConnectivityListener: NetworkConnectivityListener


    override fun onCreate() {
        super.onCreate()
        serviceLocator = ServiceLocator(
            applicationContext,
            FirebaseFirestore.getInstance()
        )
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
}