package com.tekskills.st_tekskills.utils.services

import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager


class MyPhoneState : PhoneStateListener() {
    override fun onCallStateChanged(state: Int, incomingNumber: String) {
        println("Icoming Number inside onCallStateChange : $incomingNumber")
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> println("PHONE RINGING.........TAKE IT.........")
            TelephonyManager.CALL_STATE_OFFHOOK -> println("CALL_STATE_OFFHOOK...........")
        }
    }
}
