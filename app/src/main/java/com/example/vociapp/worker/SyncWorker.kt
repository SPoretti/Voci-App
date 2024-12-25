package com.example.vociapp.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.vociapp.di.ServiceLocator
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SyncWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncWorker", "SyncWorker started")
        return try {
            val serviceLocator = ServiceLocator.getInstance()
            syncAllPendingActions(serviceLocator)
            Result.success().also {
                Log.d("SyncWorker", "SyncWorker completed successfully")
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "SyncWorker failed", e)
            Result.retry()
        }
    }

    private suspend fun syncAllPendingActions(serviceLocator: ServiceLocator){

        coroutineScope { // Create a new coroutine scope
            Log.d("SyncWorker", "Starting homelessRepository.syncPendingActions()")
            val homelessDeferred = async<Unit> {
                serviceLocator.getHomelessRepository().syncPendingActions()
            }
            Log.d("SyncWorker", "Starting requestRepository.syncPendingActions()")

            val requestDeferred = async<Unit> {
                serviceLocator.getRequestRepository().syncPendingActions()
            }
            Log.d("SyncWorker", "Starting volunteerRepository.syncPendingActions()")

            val updateDeferred = async<Unit> {
                serviceLocator.getUpdatesRepository().syncPendingActions()
            }
            Log.d("SyncWorker", "Starting volunteerRepository.syncPendingActions()")

            val volunteerDeferred = async<Unit> {
                serviceLocator.getVolunteerRepository().syncPendingActions()
            }

            awaitAll(
                homelessDeferred,
                requestDeferred,
                updateDeferred,
                volunteerDeferred,
            ) // Wait for all to complete
        }
    }
}
