package com.example.tasks.jobscheduler

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import java.util.*

@SuppressLint("SpecifyJobSchedulerIdRange")
class ExampleJobService : JobService() {
    override fun onStartJob(params: JobParameters): Boolean {
        // We don't do any real 'work' in this sample app. All we'll
        // do is track which jobs have landed on our service, and
        // update the UI accordingly.
        Log.i(TAG, "on start job: " + params.jobId)
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        Log.i(TAG, "on stop job: " + params.jobId)
        return true
    }

    var mActivity: JobScheduler? = null
    private val jobParamsMap = LinkedList<JobParameters>()
    fun setUiCallback(activity: JobScheduler?) {
        mActivity = activity
    }

    companion object {
        private const val TAG = "SyncService"
    }
}