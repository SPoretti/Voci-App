package com.example.vociapp.data.util

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.vociapp.data.repository.HomelessRepository
import com.example.vociapp.data.repository.RequestRepository
import com.example.vociapp.data.repository.VolunteerRepository

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val homelessRepository: HomelessRepository,
    private val volunteerRepository: VolunteerRepository,
    private val requestRepository: RequestRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncWorker", "SyncWorker started")
        return try {
            homelessRepository.syncPendingActions()
            volunteerRepository.syncPendingActions()
            Result.success().also {
                Log.d("SyncWorker", "SyncWorker completed successfully")
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "SyncWorker failed", e)
            Result.retry()
        }
    }
}
