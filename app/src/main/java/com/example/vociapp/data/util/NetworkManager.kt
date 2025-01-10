package com.example.vociapp.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

class NetworkManager @Inject constructor(
    private val context: Context
) {
    /**
     * Checks if the device is connected to the internet.
     *
     * This function retrieves the ConnectivityManager system service and uses it to
     * determine if the device has an active network connection with internet
     * capabilities.
     *
     * @return `true` if the device is connected to the internet, `false` otherwise.
     */
    fun isNetworkConnected(): Boolean {
        // Get the ConnectivityManager system service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Get the active network
        val network = connectivityManager.activeNetwork ?: return false

        // Get the network capabilities
        val capabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        // Check if the network has internet capabilities
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
