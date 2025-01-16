package com.voci.app.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.voci.app.worker.SyncWorker

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


    /**
     * Triggers a synchronization operation using WorkManager.
     *
     * This function creates a one-time work request for the `SyncWorker` and enqueues it
     * with WorkManager. This will schedule the worker to run in the background,
     * performing the necessary synchronization tasks.
     */
    private fun triggerSync() {
        // Create a one-time work request for the SyncWorker
        val syncRequest: WorkRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()

        // Enqueue the work request with WorkManager
        WorkManager
            .getInstance(context)
            .enqueue(syncRequest)

        // Log a message indicating that the SyncWorker has been triggered
        Log.d("NetworkConnectivity", "SyncWorker triggered")
    }

}
