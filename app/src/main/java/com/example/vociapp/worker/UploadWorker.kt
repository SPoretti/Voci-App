package com.example.vociapp.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class UploadWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        // Do the work here--in this case, upload the images.
        Log.d("UploadWorker", "Uploading images")

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}