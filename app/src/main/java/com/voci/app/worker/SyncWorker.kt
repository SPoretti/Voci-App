package com.voci.app.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.voci.app.di.ServiceLocator
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * A worker class responsible for synchronizing data in the background.
 *
 * This worker is used to perform periodic or one-time synchronization of data
 * between the local database and remote data sources. It utilizes coroutines
 * for asynchronous operations and WorkManager for scheduling and managing
 * the work.
 */
class SyncWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    /**
     * Performs the synchronization work.
     *
     * This function is called by WorkManager when the worker is executed. It
     * retrieves the necessary dependencies, synchronizes all pending actions,
     * and returns a result indicating success or failure.
     *
     * @return A `Result` object indicating the outcome of the synchronization.
     */
    override suspend fun doWork(): Result {
        Log.d("SyncWorker", "SyncWorker started")
        return try {
            // Get the ServiceLocator instance
            val serviceLocator = ServiceLocator.getInstance()

            // Synchronize all pending actions
            syncAllPendingActions(serviceLocator)

            // Return success result
            Result.success().also {
                Log.d("SyncWorker", "SyncWorker completed successfully")
            }
        } catch (e: Exception) {
            // Log the error and return retry result
            Log.e("SyncWorker", "SyncWorker failed", e)
            Result.retry()
        }
    }

    /**
     * Synchronizes all pending actions for various repositories.
     *
     * This function launches coroutines to synchronize pending actions for
     * homeless, request, update, and volunteer repositories. It uses
     * `coroutineScope` to create a new scope for the coroutines and `async`
     * to launch them concurrently. `awaitAll` is used to wait for all
     * coroutines to complete before returning.
     *
     * @param serviceLocator The ServiceLocator instance used to obtain repositories.
     */
    private suspend fun syncAllPendingActions(serviceLocator: ServiceLocator) {
        coroutineScope { // Create a new coroutine scope
            Log.d("SyncWorker", "Starting homelessRepository.syncPendingActions()")
            val homelessDeferred = async<Unit> {
                serviceLocator.obtainHomelessRepository().syncPendingActions()
            }
            Log.d("SyncWorker", "Starting volunteerRepository.syncPendingVolunteerActions()")
            val volunteerDeferred = async<Unit> {
                serviceLocator.obtainVolunteerRepository().syncPendingVolunteerActions()
            }
            Log.d("SyncWorker", "Starting requestRepository.syncPendingActions()")

            val requestDeferred = async<Unit> {
                serviceLocator.obtainRequestRepository().syncPendingActions()
            }
            Log.d("SyncWorker", "Starting updateRepository.syncPendingActions()")

            val updateDeferred = async<Unit> {
                serviceLocator.obtainUpdatesRepository().syncPendingActions()
            }
            Log.d("SyncWorker", "Starting volunteerRepository.syncPendingPreferenceActions()")
            val preferenceDeferred = async<Unit> {
                serviceLocator.obtainVolunteerRepository().syncPendingPreferenceActions()
            }


            // Wait for all coroutines to complete
            awaitAll(
                homelessDeferred,
                requestDeferred,
                updateDeferred,
                volunteerDeferred,
                preferenceDeferred
            )
        }
    }
}
