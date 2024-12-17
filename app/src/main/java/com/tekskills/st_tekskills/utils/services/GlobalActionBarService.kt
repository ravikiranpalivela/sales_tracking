package com.tekskills.st_tekskills.utils.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.ActivityManager
import android.os.Handler
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class GlobalActionBarService : AccessibilityService() {
    var interval: Int = 100
    var maxIdleTime: Int = 20000
    var idleTime: Int = 0
    var mHandler: Handler? = null
    var cProcess: String? = null

    override fun onAccessibilityEvent(accessibilityEvent: AccessibilityEvent) {
        // TODO Auto-generated method stub
        Log.v("IdleService", "reseting idle time")
        getEventType(accessibilityEvent)
        idleTime = 0
    }

    override fun onInterrupt() {
        // TODO Auto-generated method stub
        Log.v("IdleService", "INTERRUPTED")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        println("Accessibility was connected!")
        instance = this

        val info = AccessibilityServiceInfo()
        // we are interested in all types of accessibility events
        info.describeContents()
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK
        // we want to receive events in a certain interval
        info.notificationTimeout = interval.toLong()

        serviceInfo = info

        Log.e("IdleService", " idle service connected!")
        statusChecker.run()
    }

    private fun getEventType(event: AccessibilityEvent): String {
        when (event.eventType) {
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> return "TYPE_NOTIFICATION_STATE_CHANGED"
            AccessibilityEvent.TYPE_VIEW_CLICKED -> return "TYPE_VIEW_CLICKED"
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> return "TYPE_VIEW_FOCUSED"
            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> return "TYPE_VIEW_LONG_CLICKED"
            AccessibilityEvent.TYPE_VIEW_SELECTED -> return "TYPE_VIEW_SELECTED"
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> return "TYPE_WINDOW_STATE_CHANGED"
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> return "TYPE_VIEW_TEXT_CHANGED"
        }
        return "default"
    }

    fun doAction() {
        performGlobalAction(GLOBAL_ACTION_RECENTS)
    }

    var statusChecker: Runnable = object : Runnable {
        override fun run() {
            // TODO Auto-generated method stub
            val am = applicationContext
                .getSystemService(ACTIVITY_SERVICE) as ActivityManager

            val processList = am
                .getRunningTasks(5)

            cProcess = processList[0].baseActivity!!.packageName

            Log.v("IdleService", "Current PackageName:$cProcess")
            cProcess = processList[0].baseActivity!!.className

            Log.v("IdleService", "Current ClassName:$cProcess")
            idleTime += 100
            Log.v("IdleService", "TICK TOCK, user Idle for:$idleTime")
            if (idleTime >= maxIdleTime) {
                Log.v("IdleService", "we timed out do to inactivity")
                //mHandler.postDelayed(statusChecker, interval);
                //stopSelf();
            } else {
            }
            mHandler!!.postDelayed(this, interval.toLong())
        }
    }

    companion object {
        var instance: GlobalActionBarService? = null
    }
}