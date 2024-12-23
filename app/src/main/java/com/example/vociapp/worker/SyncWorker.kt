package com.example.vociapp.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.vociapp.di.ServiceLocator

class SyncWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncWorker", "SyncWorker started")
        return try {
            syncAllPendingActions()
            Result.success().also {
                Log.d("SyncWorker", "SyncWorker completed successfully")
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "SyncWorker failed", e)
            Result.retry()
        }
    }

    private suspend fun syncAllPendingActions(){
        val homelessRepository = ServiceLocator.getInstance().getHomelessRepository()
        val volunteerRepository = ServiceLocator.getInstance().getVolunteerRepository()
        val requestRepository = ServiceLocator.getInstance().getRequestRepository()

        homelessRepository.syncPendingActions()
        volunteerRepository.syncPendingActions()
        //requestRepository.syncPendingActions()
    }
}
