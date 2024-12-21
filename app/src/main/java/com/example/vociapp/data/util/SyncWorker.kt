package com.example.vociapp.data.util

import android.content.Context
import androidx.work.CoroutineWorker
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

        // Attempt to sync pending actions
        homelessRepository.syncPendingActions()
        volunteerRepository.syncPendingActions()
        //requestRepository.syncPendingActions()

        return Result.success() // Indicate that the work was successful
    }
}
