package com.tekskills.st_tekskills.utils.services

import android.content.Context
import android.net.ConnectivityManager

object ConnectionHelper {
    var lastNoConnectionTs: Long = -1
    var isOnline: Boolean = true

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnected
    }

    fun isConnectedOrConnecting(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
    }
}
