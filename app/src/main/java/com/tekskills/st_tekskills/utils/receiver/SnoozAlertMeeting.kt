package com.tekskills.st_tekskills.utils.receiver

import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tekskills.st_tekskills.R
import com.tekskills.st_tekskills.data.db.DatabaseHelper
import java.util.Calendar

class SnoozAlertMeeting : AppCompatActivity() {
    private var mToolbar: Toolbar? = null
    var YesButton: Button? = null
    var NoButton: Button? = null
    var saveAlert: Button? = null
    var relative_yes: LinearLayout? = null
    var NextAlert: TextView? = null
    var myName: TextView? = null
    var db: DatabaseHelper? = null
    private val mHour = 0
    private val mMinute = 0
    var NEWTIME: String? = null
    var Meeting_Time: String? = null
    var LeadOfMeet: String? = null
    var TitleOfMeet: String? = null
    var Meeting_Date: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.snooz_alert_meeting)

        mToolbar = findViewById(R.id.toolbar_snooz) as Toolbar?
        setSupportActionBar(mToolbar)
        val bar: ActionBar = supportActionBar!!
        bar.elevation = 0F

        YesButton = findViewById(R.id.yes) as Button?
        NoButton = findViewById(R.id.no) as Button?
        saveAlert = findViewById(R.id.save_button) as Button?
        relative_yes = findViewById(R.id.layout_yes) as LinearLayout?
        NextAlert = findViewById(R.id.new_notif_timing) as TextView?
        db = DatabaseHelper(this@SnoozAlertMeeting)

        val myIntent: Intent = getIntent()
        TitleOfMeet = myIntent.getStringExtra("Title_meeting")
        LeadOfMeet = myIntent.getStringExtra("Title_Lead")
        Meeting_Time = myIntent.getStringExtra("Meeting_TIme")
        Meeting_Date = myIntent.getStringExtra("Meeting_Date")

        YesButton!!.setOnClickListener { relative_yes!!.visibility = View.VISIBLE }
        NoButton!!.setOnClickListener { finish() }

        NextAlert!!.setOnClickListener {
            myName = NextAlert
            showDialog(TIME_DIALOG_ID)
        }

        saveAlert!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (NEWTIME != null) {
                    if (NEWTIME!!.compareTo(NEWTIME!!) >= 1) {
                        if (NEWTIME!!.compareTo(Meeting_Time!!) < 1) {
                            db!!.updateEvent_Notif(
                                TitleOfMeet!!,
                                LeadOfMeet!!,
                                Meeting_Time!!,
                                Meeting_Date!!,
                                NEWTIME
                            )
                            addEventToPhoneDefaultCalender(
                                TitleOfMeet,
                                Meeting_Date,
                                Meeting_Time,
                                NEWTIME!!
                            )
                            Toast.makeText(this@SnoozAlertMeeting,
                                "Alert set successfully",
                                Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@SnoozAlertMeeting,
                                "Next Alert Time before the Meeting",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@SnoozAlertMeeting,
                            "Next Alert Time Should be greater than current Time",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@SnoozAlertMeeting,
                        "Please Select Time For Next Alert",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    override fun onCreateDialog(id: Int): Dialog? {
        when (id) {
            TIME_DIALOG_ID -> return TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false)
        }
        return null
    }

    private val mTimeSetListener = OnTimeSetListener { view, hourOfDay, min ->
        hour = hourOfDay
        minute = min
        // Set the Selected Date in Select date Button
        var am_pm = "AM"
        var st_hours = hour.toString()
        var st_min = minute.toString()
        if (hour >= 12) {
            am_pm = "PM"
        }
        if (hour < 10) {
            st_hours = "0" + hour
        }
        if (minute < 10) {
            st_min = "0" + minute
        }
        NEWTIME = "$st_hours-$st_min $am_pm"
        myName!!.text = "$st_hours:$st_min $am_pm"
    }
    val currentTime: String
        get() {
            val c = Calendar.getInstance()
            val Hr24 = c[Calendar.HOUR_OF_DAY]
            var Min = c[Calendar.MINUTE]
            var AmPm = "AM"
            if (Min < 10) {
                Min = "0$Min".toInt()
            }
            var dateString: String? = null
            if (Hr24 >= 12) {
                AmPm = "PM"
                dateString = "$Hr24-$Min PM"
            } else {
                dateString = "$Hr24-$Min AM"
            }
            return dateString
        }

    fun addEventToPhoneDefaultCalender(
        title: String?,
        date: String?,
        time: String?,
        notif: String
    ) {
        val EVENTS_URI = Uri.parse(getCalendarUriBase(this) + "events")
        val cr: ContentResolver = getContentResolver()

        var values = ContentValues()
        values.put("calendar_id", 1)
        values.put("title", title)
        values.put("allDay", 0)

        val beginTime = Calendar.getInstance()

        val items1 = date!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val d1 = items1[0]
        val m1 = items1[1]
        val y1 = items1[2]
        val d = d1.toInt()
        val m = m1.toInt()
        val y = y1.toInt()

        val notif11 = notif.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val temp1 = notif11[0]
        val n = temp1.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val notif_hh = n[0]
        val notif_mm = n[1]
        val hrs = notif_hh.toInt()
        val minu = notif_mm.toInt()

        beginTime[y, m - 1, d, hrs] = minu
        val startMillis = beginTime.timeInMillis
        val endTime = Calendar.getInstance()
        endTime[y, m - 1, d, hrs + 3] = 0
        val endMillis = endTime.timeInMillis

        values.put("dtstart", startMillis) // event starts at 11 minutes from now
        values.put("dtend", endMillis) // ends 60 minutes from now
        values.put("description", "Reminder description")
        values.put("hasAlarm", 1)

        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Indian/Christmas")
        val event = cr.insert(EVENTS_URI, values)

        val REMINDERS_URI = Uri.parse(getCalendarUriBase(this) + "reminders")
        values = ContentValues()
        values.put("event_id", event!!.lastPathSegment!!.toLong())
        values.put("method", 1)
        values.put("minutes", 1)
        cr.insert(REMINDERS_URI, values)
    }

    private fun getCalendarUriBase(act: Activity): String? {
        var calendarUriBase: String? = null
        var calendars = Uri.parse("content://calendar/calendars")
        var managedCursor: Cursor? = null
        try {
            managedCursor = act.managedQuery(calendars, null, null, null, null)
        } catch (e: Exception) {
        }
        if (managedCursor != null) {
            calendarUriBase = "content://calendar/"
        } else {
            calendars = Uri.parse("content://com.android.calendar/calendars")
            try {
                managedCursor = act.managedQuery(calendars, null, null, null, null)
            } catch (e: Exception) {
            }
            if (managedCursor != null) {
                calendarUriBase = "content://com.android.calendar/"
            }
        }
        return calendarUriBase
    }

    companion object {
        const val TIME_DIALOG_ID: Int = 1
        var year: Int = 0
        var month: Int = 0
        var hour: Int = 0
        var minute: Int = 0
    }
}
