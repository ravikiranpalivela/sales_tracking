package com.tekskills.st_tekskills.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    var v: Vibrator? = null
    var ct: Context? = null
    var title: String? = null


    override fun onReceive(context: Context, intent: Intent) {
        // TODO Auto-generated method stub

        ct = context

        Log.v("## onReceive", "## Intent : $intent")
        Toast.makeText(context, "OnReceive alarm test", Toast.LENGTH_SHORT).show()

        v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v!!.vibrate(3000)


        //Alarm RingTone
        //Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //Ringtone ringtoneAlarm = RingtoneManager.getRingtone(context, alarmTone);

        //Play Alarm even in Silent mode
        //ringtoneAlarm.setStreamType(AudioManager.STREAM_ALARM);

        //ringtoneAlarm.play();

        //Log.v("## onReceive RT", "## onReceive Ringtone: "+alarmTone);
    }
}
