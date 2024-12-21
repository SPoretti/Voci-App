package com.example.vociapp.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class NetworkConnectivityListener(private val context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            // Network is available, trigger the sync operation
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
        // Enqueue a OneTimeWorkRequest for the sync operation when the network is available
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
        WorkManager.getInstance(context).enqueue(syncRequest)
    }
}
