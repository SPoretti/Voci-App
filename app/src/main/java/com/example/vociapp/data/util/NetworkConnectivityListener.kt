package com.example.vociapp.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.vociapp.worker.SyncWorker
import java.util.concurrent.TimeUnit

class NetworkConnectivityListener(private val context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            // Network is available, trigger the sync operation
            Log.d("NetworkConnectivity", "Network is available")
            triggerSync()
        }

        override fun onLost(network: Network) {
            // Network is lost, you can handle this if necessary (optional)
        }
    }

    fun startMonitoring() {
        // Register the network callback for monitoring connectivity changes
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    fun stopMonitoring() {
        // Unregister the network callback when no longer needed
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun triggerSync() {
        val syncRequest: WorkRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
        WorkManager
            .getInstance(context)
            .enqueue(syncRequest)
        Log.d("NetworkConnectivity", "SyncWorker triggered")
//        val periodicSyncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
//            5, TimeUnit.MINUTES // Adjust the interval as needed
//        ).build()
//
//        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//            "PeriodicSync",
//            ExistingPeriodicWorkPolicy.KEEP, // Keep the periodic work schedule
//            periodicSyncRequest
//        )
//        Log.d("NetworkConnectivity", "SyncWorker triggered")
    }

}
