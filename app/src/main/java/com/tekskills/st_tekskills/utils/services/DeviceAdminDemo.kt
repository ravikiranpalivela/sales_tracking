package com.tekskills.st_tekskills.utils.services

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent

class DeviceAdminDemo : DeviceAdminReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
    }

    override fun onEnabled(context: Context, intent: Intent) {
    }

    override fun onDisabled(context: Context, intent: Intent) {
    }
}