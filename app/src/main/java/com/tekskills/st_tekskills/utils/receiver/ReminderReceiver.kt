package com.tekskills.st_tekskills.utils.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.tekskills.st_tekskills.R
import com.tekskills.st_tekskills.data.db.DatabaseHelper
import com.tekskills.st_tekskills.presentation.activities.MainActivity
import java.util.Calendar
import java.util.GregorianCalendar

class ReminderReceiver : BroadcastReceiver() {
    var Title_Lead: HashMap<String, String>? = null
    var db: DatabaseHelper? = null

    override fun onReceive(context: Context, intent: Intent) {
        Title_Lead = HashMap()
        db = DatabaseHelper(context)
        val bundle = intent.extras
        currentTime
        if (!Title_Lead!!.isEmpty()) {
            val now = GregorianCalendar.getInstance()
            context.contentResolver.delete(
                Uri.parse("content://com.android.calendar/events"),
                "calendar_id=? and title=? ", arrayOf(1.toString(), Title_Lead!!["EV_TITLE"])
            )
            val dayOfWeek = now[Calendar.DATE]
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            if (dayOfWeek != 1 && dayOfWeek != 7) {
                val mBuilder =
                    NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.calendarfinalgreen)
                        .setAutoCancel(true)
                        .setColor(Color.parseColor("#7DCD50"))
                        .setContentTitle(Title_Lead!!["EV_TITLE"])
                        .setSound(uri)
                        .setContentText(" with " + Title_Lead!!["EV_LEAD"])

                val resultIntent = Intent(context, SnoozAlertMeeting::class.java)
                resultIntent.putExtra("Title_meeting", Title_Lead!!["EV_TITLE"])
                resultIntent.putExtra("Title_Lead", Title_Lead!!["EV_LEAD"])
                resultIntent.putExtra("Meeting_TIme", Title_Lead!!["EV_TIME"])
                resultIntent.putExtra("Meeting_Date", Title_Lead!!["EV_DATE"])

                val stackBuilder = TaskStackBuilder.create(context)
                stackBuilder.addParentStack(MainActivity::class.java) //MainActivity
                stackBuilder.addNextIntent(resultIntent)
                val resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                mBuilder.setContentIntent(resultPendingIntent)
                val mNotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                mNotificationManager.notify(1, mBuilder.build())
            }
        }
    }

    val currentTime: Unit
        get() {
            val c = Calendar.getInstance()
            val Hr24 = c[Calendar.HOUR_OF_DAY]
            var Min = c[Calendar.MINUTE]
            var AmPm: String? = null
            if (Min < 10) {
                Min = "0$Min".toInt()
            }
            var dateString: String? = null
            if (Hr24 >= 12) {
                AmPm = "PM"
                dateString = "$Hr24-$Min PM"
            } else {
                AmPm = "AM"
                dateString = "$Hr24-$Min AM"
            }

            Title_Lead = db!!.getTitle_Lead(dateString)
            if (Title_Lead!!.isEmpty()) {
                Min = Min + 1
                if (Min < 10) {
                    Min = "0$Min".toInt()
                }
                Title_Lead = db!!.getTitle_Lead("$Hr24-$Min $AmPm")
                if (Title_Lead!!.isEmpty()) {
                    Min = Min + 1
                    if (Min < 10) {
                        Min = "0$Min".toInt()
                    }
                    Title_Lead = db!!.getTitle_Lead("$Hr24-$Min $AmPm")
                    if (Title_Lead!!.isEmpty()) {
                        Min = Min + 1
                        Title_Lead = db!!.getTitle_Lead("$Hr24-$Min $AmPm")
                        if (Title_Lead!!.isEmpty()) {
                            Min = Min + 1
                            Title_Lead = db!!.getTitle_Lead("$Hr24-$Min $AmPm")
                        }
                    }
                }
            }
        }
}
