package com.example.tasks.jobscheduler

import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import com.example.tasks.R


class JobScheduler : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_scheduler)
        val res = resources
        defaultColor = res.getColor(R.color.none_received)
        startJobColor = res.getColor(R.color.start_received)
        stopJobColor = res.getColor(R.color.stop_received)
        mDelayEditText = findViewById<View>(R.id.delay_time) as EditText
        mDeadlineEditText = findViewById<View>(R.id.deadline_time) as EditText
        mWiFiConnectivityRadioButton = findViewById<View>(R.id.btn_checkbox_unmetered) as RadioButton
        mAnyConnectivityRadioButton = findViewById<View>(R.id.btn_checkbox_any) as RadioButton
        mRequiresChargingCheckBox = findViewById<View>(R.id.checkbox_charging) as CheckBox
        mRequiresIdleCheckbox = findViewById<View>(R.id.checkbox_idle) as CheckBox
        mServiceComponent = ComponentName(this, ExampleJobService::class.java)
    }

    // UI fields.
    private var defaultColor = 0
    private var startJobColor = 0
    private var stopJobColor = 0
    private var mDelayEditText: EditText? = null
    private var mDeadlineEditText: EditText? = null
    private var mWiFiConnectivityRadioButton: RadioButton? = null
    private var mAnyConnectivityRadioButton: RadioButton? = null
    private var mRequiresChargingCheckBox: CheckBox? = null
    private var mRequiresIdleCheckbox: CheckBox? = null
    private var mServiceComponent: ComponentName? = null

    var mTestService: ExampleJobService? = null

    fun scheduleJob(v: View?) {
        val builder = JobInfo.Builder(
            kJobId++,
            mServiceComponent!!
        )
        val delay = mDelayEditText!!.text.toString()
        if (!TextUtils.isEmpty(delay)) {
            builder.setMinimumLatency(java.lang.Long.valueOf(delay) * 1000)
        }
        val deadline = mDeadlineEditText!!.text.toString()
        if (!TextUtils.isEmpty(deadline)) {
            builder.setOverrideDeadline(java.lang.Long.valueOf(deadline) * 1000)
        }
        val requiresUnmetered = mWiFiConnectivityRadioButton!!.isChecked
        val requiresAnyConnectivity = mAnyConnectivityRadioButton
            ?.isChecked
        if (requiresUnmetered) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
        } else if (requiresAnyConnectivity == true) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        }
        builder.setRequiresDeviceIdle(mRequiresIdleCheckbox!!.isChecked)
        builder.setRequiresCharging(mRequiresChargingCheckBox!!.isChecked)
        val jobScheduler = application.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
    }

    fun cancelAllJobs(v: View?) {
        val tm = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        tm.cancelAll()
    }

    companion object {
        const val MSG_UNCOLOUR_START = 0
        const val MSG_UNCOLOUR_STOP = 1
        const val MSG_SERVICE_OBJ = 2
        private var kJobId = 0
    }
}