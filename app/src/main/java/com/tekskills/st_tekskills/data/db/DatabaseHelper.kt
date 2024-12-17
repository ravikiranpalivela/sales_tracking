package com.tekskills.st_tekskills.data.db

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.tekskills.st_tekskills.R
import com.tekskills.st_tekskills.utils.receiver.CommonData
import com.tekskills.st_tekskills.utils.receiver.ShowEventDetailsActivity
import java.util.Date
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class DatabaseHelper(var mcontext: Context) :
    SQLiteOpenHelper(mcontext, DATABASE_NAME, null, DATABASE_VERSION) {
    /**
     * RK CREATED
     * Create Table
     */
    override fun onCreate(db: SQLiteDatabase) {
        // creating required tables
        db.execSQL(CREATE_TABLE_EVENTS)
        db.execSQL(CREATE_TABLE_LEADS)
        db.execSQL(CREATE_TABLE_LATLONG)
        db.execSQL(CREATE_TABLE_CHECKIN)
        db.execSQL(CREATE_TABLE_COMPANIES)
    }

    /**
     * Upgrade Table
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS TABLE_CAL_EVENTS")
        db.execSQL(CREATE_TABLE_EVENTS)

        //db.execSQL("DROP TABLE IF EXISTS "+ CREATE_TABLE_LEADS);
        //db.execSQL("DROP TABLE IF EXISTS "+ CREATE_TABLE_LATLONG);
        onCreate(db)
    }

    /**
     * RK CREATED
     * Inserting Event Data
     */
    fun addEvent(
        Event_Title: String,
        Recipient: String,
        StartDate: String,
        StartTime: String,
        MinuteBefore: String,
        Description: String,
        Mobile: String,
        Email: String,
        companyNAme: String,
        flag: Int,
        Location: String,
        latitude: String?,
        longitude: String?,
        ev_uid: String?,
        outcomeOfCall: String,
        highlights: String,
        projectValue: String,
        status: String
    ) {
        Log.d(
            "============in db===", "Event_Title==" + Event_Title + "Recipient==" + Recipient
                    + "StartDate==" + StartDate + "StartTime==" + StartTime + "MinuteBefore==" + MinuteBefore
                    + "Description==" + Description + "Mobile==" + Mobile + "Email==" + Email + "companyNAme==" + companyNAme
                    + "Location==" + Location + "outcomeOfCall==" + outcomeOfCall + "highlights==" + highlights
                    + "projectValue==" + projectValue + "status==" + status
        )

        val db = this.writableDatabase
        val values = ContentValues()

        val items1 = StartDate.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val d1 = items1[0]
        val m1 = items1[1]
        val y1 = items1[2]
        Log.d("==4421===", "======$d1-$m1-$y1")
        val d = d1.toInt()
        val m = m1.toInt()
        val y = y1.toInt()
        values.put("eventDate_day", d)
        values.put("eventDate_month", m)
        values.put("eventDate_year", y)
        values.put("eventDate", StartDate)
        values.put("eventTime", StartTime)
        values.put("eventTitle", Event_Title)
        values.put("eventRecipient", Recipient)
        values.put("eventDescription", Description)
        values.put("eventContactNo", Mobile)
        values.put("eventEmail", Email)
        values.put("eventNotifBefore", MinuteBefore)
        values.put("eventLocation_lat", latitude)
        values.put("eventLocation_long", longitude)
        values.put("eventLocation", Location)
        values.put(EV_OUTCOMECALL, outcomeOfCall)
        values.put(EV_DISCUSSION_HIGHLIGHTS, highlights)
        values.put(EV_PROJECTVALUE, projectValue)
        values.put("companyName", companyNAme)
        values.put("meeting_satatus", status)
        values.put(EV_UID, ev_uid)
        values.put(EV_FLAG, flag)
        Log.d("========", "jhhdjshbjfsd===$values")

        db.insert(TABLE_CAL_EVENTS, null, values)
        Log.d("========", "jhhdjshbjfsd===")
        db.close()
    }

    /**
     * RK CREATED
     * Inserting Lead Data
     */
    fun insertLead(
        st_name: String?,
        st_mobile: String?,
        st_altNum: String?,
        st_email: String?,
        st_Company: String?,
        st_officeAdd: String?,
        st_Purpose: String?,
        st_Expected: String?,
        flag: Int,
        uid: String?
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("lead_name", st_name)
        values.put("lead_mobile", st_mobile)
        values.put("lead_altername_mobile", st_altNum)
        values.put("lead_email", st_email)
        values.put("lead_company", st_Company)
        values.put("lead_office_address", st_officeAdd)
        values.put("lead_purpose_call", st_Purpose)
        values.put("lead_expecte_date", st_Expected)
        values.put("lead_flag_number", flag)
        values.put(LEAD_UID, uid)
        db.insert(TABLE_ALL_LEADS, null, values)
        db.close()
    }

    /**
     * RK CREATED
     * Inserting Lead Data
     */
    fun insertCompany(id: String?, st_name: String?) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("lead_name", st_name)
        values.put(COMPANY_ID, id)
        db.insert(TABLE_ALL_COMPANIES, null, values)
        db.close()
    }

    /**
     * RK CREATED
     * CheckIn Data Inserting
     */
    fun checkIN(
        st_meetingID: Int,
        st_mmsType: String?,
        st_data: String?,
        st_flag: String?,
        st_extension: String?,
        st_status: String?,
        st_lead_name: String?
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("meeting_id", st_meetingID)
        values.put("meeting_mms_type", st_mmsType)
        values.put("meeting_data", st_data)
        values.put("meeting_flag_number", st_flag)
        values.put("meeting_extension", st_extension)
        values.put("meeting_status", st_status)
        values.put("meeting_lead_name", st_lead_name)
        db.insert(TABLE_CHECKIN, null, values)
        val rowInserted = db.insert(TABLE_CHECKIN, null, values)
        if (rowInserted >= 0) {
            Log.v("CHECHEDIN", "Checked in Successfully")
            CommonData.CheckinStatus = true
        } else {
            Log.v("CHECKEDIN", "Checked in NotSuccessfully")
            CommonData.CheckinStatus = false
        }
        db.close()
    }

    /**
     * RK CREATED
     * Updating Meeting Data
     */
    fun updateMeeting(
        oldTitle: String,
        olddate: String,
        oldTime: String,
        Event_Title: String,
        recipients_name: String,
        StartDate: String,
        StartTime: String,
        MinuteBefore: String,
        St_description: String,
        St_mobile: String,
        St_email: String?,
        companyName: String?,
        status: String?,
        location: String?,
        outcomeCall: String?,
        highlights: String?,
        pro_value: String?,
        latitude: String?,
        longitude: String?
    ) {
        Log.d(
            "===update===",
            "olddate==" + olddate + "Event_Title==" + Event_Title + "recipients_name==" + recipients_name
                    + "StartDate==" + StartDate + "StartTime==" + StartTime + "MinuteBefore==" + MinuteBefore
                    + "St_description==" + St_description + "St_mobile==" + St_mobile //						+ "companyNAme==" + companyNAme+ "Location==" + Location+ "outcomeOfCall==" + outcomeOfCall
            //						+ "highlights==" + highlights+ "projectValue==" + projectValue+ "status==" + status
        )
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("eventDate", StartDate)
        val items1 = StartDate.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val d1 = items1[0]
        val m1 = items1[1]
        val y1 = items1[2]
        //Log.d("==4421===","======"+d1+"-"+m1+"-"+y1);
        val d = d1.toInt()
        val m = m1.toInt()
        val y = y1.toInt()
        values.put("eventDate_day", d)
        values.put("eventDate_month", m)
        values.put("eventDate_year", y)
        values.put("eventTime", StartTime)
        values.put("eventTitle", Event_Title)
        values.put("eventRecipient", recipients_name)
        values.put("eventDescription", St_description)
        values.put("eventContactNo", St_mobile)
        values.put("eventEmail", St_email)
        values.put("eventNotifBefore", MinuteBefore)
        values.put("eventLocation", location)
        values.put("eventLocation_lat", latitude)
        values.put("eventLocation_long", longitude)
        values.put("companyName", companyName)
        values.put("meeting_satatus", status)
        values.put(EV_OUTCOMECALL, outcomeCall)
        values.put(EV_DISCUSSION_HIGHLIGHTS, highlights)
        values.put(EV_PROJECTVALUE, pro_value)
        db.update(
            TABLE_CAL_EVENTS, values,
            EV_DATE + " = ? AND " + EV_TITLE + " = ? AND " + EV_TIME + " = ?",
            arrayOf(olddate, oldTitle, oldTime)
        )
        db.close()
    }

    /**
     * RK CREATED
     * Inserting LAtLong Data
     */
    fun insertLatlong(
        date: String?,
        datetime: String?,
        lat: String?,
        lon: String?,
        deviceid: String?
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(LATDATE, date)
        values.put(LATDATETIME, datetime)
        values.put(LATDEVICE, deviceid)
        values.put(LAT, lat)
        values.put(LON, lon)
        db.insert(TABLE_LATLONG, null, values)
        db.close()
    }

    val companiesName: ArrayList<String>
        /**
         * RK CREATED
         * Getting Lead Names
         */
        @SuppressLint("Range")
        get() {
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * from all_companies_list", null)
            val Names = ArrayList<String>()
            if (res.moveToFirst()) {
                do {
                    Names.add(res.getString(res.getColumnIndex(COMPANY_NAME)))
                } while (res.moveToNext())
            }
            res.close()
            return Names
        }

    val leadsName: ArrayList<String>
        /**
         * RK CREATED
         * Getting company Names
         */
        @SuppressLint("Range")
        get() {
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * from all_lead_list", null)
            val Names = ArrayList<String>()
            if (res.moveToFirst()) {
                do {
                    Names.add(res.getString(res.getColumnIndex(LEAD_Name)))
                } while (res.moveToNext())
            }
            res.close()
            return Names
        }

    /**
     * RK CREATED
     * Get Single Lead Details
     */
    @SuppressLint("Range")
    fun getLeadDetails(leadName: String): HashMap<String, String> {
        val lead_detailMap = HashMap<String, String>()
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT * from all_lead_list Where " + LEAD_Name + "=?",
            arrayOf(leadName.toString())
        )
        if (res.moveToFirst()) {
            do {
                lead_detailMap["lead_name"] = res.getString(res.getColumnIndex(LEAD_Name))
                lead_detailMap["lead_mobile"] =
                    res.getString(res.getColumnIndex(LEAD_MOBILE))
                lead_detailMap["lead_email"] = res.getString(res.getColumnIndex(LEAD_EMAIL))
                lead_detailMap["lead_altername_mobile"] =
                    res.getString(res.getColumnIndex(LEAD_ALTMOBILE))
                lead_detailMap["lead_company"] = res.getString(res.getColumnIndex(LEAD_COMPANY))
                lead_detailMap["lead_office_address"] =
                    res.getString(res.getColumnIndex(LEAD_OFFICE_ADD))
                lead_detailMap["lead_purpose_call"] =
                    res.getString(res.getColumnIndex(LEAD_PURPOSE))
                lead_detailMap["lead_expecte_date"] = res.getString(
                    res.getColumnIndex(
                        LEAD_EXPECTED_DATE
                    )
                )
                lead_detailMap["lead_uid"] = res.getString(res.getColumnIndex(LEAD_UID))
            } while (res.moveToNext())
        }
        res.close()
        return lead_detailMap
    }


    /**
     * RK CREATED
     * Getting Lead Flag Status
     */
    @SuppressLint("Range")
    fun getFlagLead(leadName: String): String {
        var titles = ""
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT * from all_lead_list Where " + LEAD_Name + "=?",
            arrayOf(leadName.toString())
        )
        if (res.moveToFirst()) {
            do {
                titles = res.getString(res.getColumnIndex(LEAD_FLAG))
            } while (res.moveToNext())
        }
        res.close()
        return titles
    }

    /**
     * RK CREATED
     * Clear Tavle Data
     */
    fun clearTavle() {
        val db = this.writableDatabase
        db.delete(TABLE_ALL_LEADS, null, null)
        db.delete(TABLE_CAL_EVENTS, null, null)
        db.close()
    }

    /**
     * RK CREATED
     * Clear CheckIn Data
     */
    fun clearCheckIn() {
        val db = this.writableDatabase
        db.execSQL("delete from " + TABLE_CHECKIN)
        db.close()
    }

    /**
     * RK CREATED
     * Get Event
     */
    fun getEvent(date: String): Int {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT * from CalenderEvents Where " + EV_DATE + "=?",
            arrayOf(date.toString())
        )
        var count = 0
        if (res.moveToFirst()) {
            do {
                count = count + 1
            } while (res.moveToNext())
        }
        res.close()
        return count
    }

    val allEventsNames: List<String>
        /**
         * RK CREATED
         * All Events Names
         */
        @SuppressLint("Range")
        get() {
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * from CalenderEvents", null)
            val meetingsList: MutableList<String> = ArrayList()
            if (res.moveToFirst()) {
                do {
                    meetingsList.add(res.getString(res.getColumnIndex(EV_TITLE)))
                } while (res.moveToNext())
            }
            res.close()
            return meetingsList
        }

    /**
     * RK CREATED
     * getMeetingsList
     */
    @SuppressLint("Range")
    fun getMeetingsList(LeadName: String): List<String> {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT * from CalenderEvents Where " + EV_RECIPIENTS + "=?",
            arrayOf(LeadName.toString())
        )
        val meetingsList: MutableList<String> = ArrayList()
        if (res.moveToFirst()) {
            do {
                meetingsList.add(res.getString(res.getColumnIndex(EV_TITLE)))
                //				meetingsList.add(res.getString(res.getColumnIndex(EV_DATE)));
            } while (res.moveToNext())
        }
        res.close()
        return meetingsList
    }

    /**
     * RK CREATED
     * Get Flag
     */
    @SuppressLint("Range")
    fun getFlag(oldTitle: String, olddate: String, oldTime: String, oldNotifTime: String?): Int {
        var flag = 0

        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT * from CalenderEvents Where " + EV_DATE + "=?" + " AND " + EV_TIME + "=?" + " AND " + EV_TITLE + "=?",
            arrayOf(olddate.toString(), oldTime.toString(), oldTitle.toString())
        )

        if (res.moveToFirst()) {
            do {
                flag = res.getInt(res.getColumnIndex(EV_FLAG))
            } while (res.moveToNext())
        }
        res.close()
        return flag
    }

    val eventAllEvents: Int
        /**
         * RK CREATED
         * Get All Leads List
         */
        get() {
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * from all_lead_list", null)
            var count = 0
            if (res.moveToFirst()) {
                do {
                    count = count + 1
                } while (res.moveToNext())
            }
            res.close()
            return count
        }

    val allCheckedInList: ArrayList<HashMap<String, String>>
        /**
         * RK CREATED
         * Get All Checked In List
         */
        @SuppressLint("Range")
        get() {
            val finalList = ArrayList<HashMap<String, String>>()
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * from check_in_list", null)
            if (res.moveToFirst()) {
                do {
                    val meetingList = HashMap<String, String>()
                    meetingList["MEETING_ID"] = res.getString(res.getColumnIndex(MEETING_ID))
                    meetingList["MMS_TYPE"] = res.getString(res.getColumnIndex(MEETING_MMSTYPE))
                    meetingList["DATA"] = res.getString(res.getColumnIndex(MEETING_DATA))
                    meetingList["FLAG"] = res.getString(res.getColumnIndex(MEETING_FLAG))
                    meetingList["EXTENSION"] =
                        res.getString(res.getColumnIndex(MEETING_EXTENSION))
                    meetingList["STATUS"] =
                        res.getString(res.getColumnIndex(MEETING_STATUS))
                    meetingList["LEAD_NAME"] =
                        res.getString(res.getColumnIndex(MEETING_LEAD_NAME))
                    Log.d("MEETINGS ALL LIST", "Hash map===$meetingList")

                    finalList.add(meetingList)
                } while (res.moveToNext())
            }
            Log.d("@@Meeting Details list", "List of meeting details===$finalList")
            res.close()
            return finalList
        }

    /**
     * RK CREATED
     * Get Meeting Date
     */
    @SuppressLint("Range")
    fun getMeetingDate(lead: String, title: String): List<String> {
        val db = this.writableDatabase

        val res = db.rawQuery(
            "SELECT * from CalenderEvents Where " + EV_TITLE + "=?" + " AND "
                    + EV_RECIPIENTS + "=?", arrayOf(title.toString(), lead.toString())
        )
        val dateof_meet: String? = null
        val myString: MutableList<String> = ArrayList()

        if (res.moveToFirst()) {
            do {
//				dateof_meet=res.getString(res.getColumnIndex(EV_DATE));
                myString.add(res.getString(res.getColumnIndex(EV_DATE)))
                myString.add(res.getString(res.getColumnIndex(EV_STATUS)))

                Log.d("getEventTitle", "getEventTitle =========== $myString")
            } while (res.moveToNext())
        }
        res.close()
        return myString
    }


    /**
     * RK CREATED
     * Meeting Details
     */
    @SuppressLint("Range")
    fun getEventDetails(date: String, title: String): HashMap<String, String> {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT * from CalenderEvents Where " + EV_DATE + "=?" + " AND " + EV_TITLE + "=?",
            arrayOf(date.toString(), title.toString())
        )
        val titles = HashMap<String, String>()
        if (res.moveToFirst()) {
            do {
                titles["Title"] = res.getString(res.getColumnIndex(EV_TITLE))
                titles["Date_day"] = res.getString(res.getColumnIndex(EV_DATE_DAY))
                titles["Date_Month"] = res.getString(res.getColumnIndex(EV_DATE_MONTH))
                titles["Date_Year"] = res.getString(res.getColumnIndex(EV_DATE_YEAR))
                titles["Recip"] = res.getString(res.getColumnIndex(EV_RECIPIENTS))
                titles["Company"] = res.getString(res.getColumnIndex(EV_COMPANY))
                titles["time"] = res.getString(res.getColumnIndex(EV_TIME))
                titles["mob"] = res.getString(res.getColumnIndex(EV_CONTACT_NO))
                titles["email"] = res.getString(res.getColumnIndex(EV_EMAIL))
                titles["desc"] = res.getString(res.getColumnIndex(EV_DESCRIPTION))
                titles["meeting_satatus"] =
                    res.getString(res.getColumnIndex(EV_STATUS))
                titles["date_of_meet"] = res.getString(res.getColumnIndex(EV_DATE))
            } while (res.moveToNext())
        }

        Log.d("getEventTitle", "getEventTitle Details======= =========== $title")
        res.close()
        return titles
    }


    val dateList: List<String>
        /**
         * RK CREATED
         * getDateList
         */
        @SuppressLint("Range")
        get() {
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * from CalenderEvents", null)
            val Dates: MutableList<String> = ArrayList()
            if (res.moveToFirst()) {
                do {
                    Dates.add(res.getString(res.getColumnIndex(EV_DATE)))
                } while (res.moveToNext())
            }
            res.close()
            return Dates
        }

    /**
     * RK CREATED
     * Updating CheckIn Status
     */
    fun updateCheckINStatus(Leadname: String, MeetingTitle: String, Flag: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(EV_CHECKIN_STATUS, "True")
        values.put(EV_STATUS, "CheckedIn")
        db.update(
            TABLE_CAL_EVENTS, values,
            EV_RECIPIENTS + " = ? AND " + EV_TITLE + " = ? AND " + EV_FLAG + " = ?",
            arrayOf(Leadname, MeetingTitle, Flag)
        )
        db.close()
    }

    /**
     * Updating Meeting Status
     */
    fun updateMeetingStatus(MeetingTitle: String, Leadname: String, Flag: String, status: String?) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(EV_STATUS, status)
        db.update(
            TABLE_CAL_EVENTS, values,
            EV_RECIPIENTS + " = ? AND " + EV_TITLE + " = ? AND " + EV_FLAG + " = ?",
            arrayOf(Leadname, MeetingTitle, Flag)
        )
        db.close()
    }

    /**
     * RK CREATED
     * Update Meeting Missed Status
     */
    fun updateMeetingMissedStatus(MeetingTitle: String, Leadname: String, status: String?) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(EV_STATUS, status)
        db.update(
            TABLE_CAL_EVENTS, values,
            EV_RECIPIENTS + " = ? AND " + EV_TITLE + " = ?",
            arrayOf(Leadname, MeetingTitle)
        )
        db.close()
    }

    /**
     * Get Leads List
     */
    @SuppressLint("Range")
    fun getRecepients(date: String): List<String> {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT * from CalenderEvents Where " + EV_DATE + "=?",
            arrayOf(date.toString())
        )
        val titles: MutableList<String> = ArrayList()
        if (res.moveToFirst()) {
            do {
                titles.add(res.getString(res.getColumnIndex(EV_TITLE)))
            } while (res.moveToNext())
        }
        res.close()
        return titles
    }

    /**
     * RK CREATED
     * Calender Data
     */
    @SuppressLint("Range")
    fun getData(title: String, Recipient: String, Date: String): HashMap<String, String> {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT * from CalenderEvents Where " + EV_DATE + "=?" + " AND " + EV_RECIPIENTS + "=?" + " AND " + EV_TITLE + "=?",
            arrayOf(Date.toString(), Recipient.toString(), title.toString())
        )
        val titles = HashMap<String, String>()
        if (res.moveToFirst()) {
            do {
                titles["Title"] = res.getString(res.getColumnIndex(EV_TITLE))
                titles["Date"] = res.getString(res.getColumnIndex(EV_DATE))
                titles["Date_day"] = res.getString(res.getColumnIndex(EV_DATE_DAY))
                titles["Date_Month"] = res.getString(res.getColumnIndex(EV_DATE_MONTH))
                titles["Date_Year"] = res.getString(res.getColumnIndex(EV_DATE_YEAR))
                titles["Recip"] = res.getString(res.getColumnIndex(EV_RECIPIENTS))
                titles["Company"] = res.getString(res.getColumnIndex(EV_COMPANY))
                titles["time"] = res.getString(res.getColumnIndex(EV_TIME))
                titles["notif_time"] =
                    res.getString(res.getColumnIndex(EV_NOTIF_BEFORE))
                titles["mob"] = res.getString(res.getColumnIndex(EV_CONTACT_NO))
                titles["email"] = res.getString(res.getColumnIndex(EV_EMAIL))
                titles["desc"] = res.getString(res.getColumnIndex(EV_DESCRIPTION))
                titles["loc"] = res.getString(res.getColumnIndex(EV_PLACE))
                titles["status"] = res.getString(res.getColumnIndex(EV_STATUS))
                titles["meetingID"] = res.getString(res.getColumnIndex(EV_UID))
                titles["call_outcome"] = res.getString(res.getColumnIndex(EV_OUTCOMECALL))
                titles["dis_highlights"] =
                    res.getString(res.getColumnIndex(EV_DISCUSSION_HIGHLIGHTS))
                titles["pro_value"] = res.getString(res.getColumnIndex(EV_PROJECTVALUE))
            } while (res.moveToNext())
        }
        res.close()
        return titles
    }

    /**
     * RK CREATED
     * Updating Lead Details
     */
    fun updateSingleLEAD(
        st_name: String?,
        st_mobile: String?,
        st_altNum: String?,
        st_email: String?,
        st_Company: String?,
        st_officeAdd: String?,
        st_Purpose: String?,
        st_Expected: String?,
        uid: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("lead_name", st_name)
        values.put("lead_mobile", st_mobile)
        values.put("lead_altername_mobile", st_altNum)
        values.put("lead_email", st_email)
        values.put("lead_company", st_Company)
        values.put("lead_office_address", st_officeAdd)
        values.put("lead_purpose_call", st_Purpose)
        values.put("lead_expecte_date", st_Expected)
        db.update(
            TABLE_ALL_LEADS, values,
            LEAD_UID + " = ?",
            arrayOf(uid)
        )

        db.close()
    }

    /**
     * RK CREATED
     * Get Leads List
     */
    @SuppressLint("Range")
    fun getlead(): ArrayList<HashMap<String, String>> {
        val db = this.writableDatabase

        val list = ArrayList<HashMap<String, String>>()
        var hashmap: HashMap<String, String>
        db.isOpen
        try {
            val res = db.rawQuery("SELECT * from all_lead_list", null)
            if (res.moveToFirst()) {
                do {
                    hashmap = HashMap()
                    hashmap["LEAD_Name"] = res.getString(res.getColumnIndex(LEAD_Name))
                    hashmap["EMAIL"] = res.getString(res.getColumnIndex(LEAD_EMAIL))
                    hashmap["MOBILE"] = res.getString(res.getColumnIndex(LEAD_MOBILE))
                    hashmap["COMPANY"] = res.getString(res.getColumnIndex(LEAD_COMPANY))
                    hashmap["OFFICEADD"] = res.getString(res.getColumnIndex(LEAD_OFFICE_ADD))
                    hashmap["DATE"] =
                        res.getString(res.getColumnIndex(LEAD_EXPECTED_DATE))

                    list.add(hashmap)
                } while (res.moveToNext())
            }
            res.close()
        } finally {
            db.close()
        }
        return list
    }

    /**
     * RK CREATED
     * Checking For Date and Time Present
     */
    fun checkDateTimePresent(Date: String, Time: String): Boolean {
        var returnVar = false
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT * from CalenderEvents Where " + EV_DATE + "=?" + " AND " + EV_TIME + "=?",
            arrayOf(Date.toString(), Time.toString())
        )
        if (res.count > 0) {
            returnVar = true
        }
        res.close()
        return returnVar
    }

    /**
     * RK CREATED
     * Getting LatLong List
     */
    fun getlatlong(): ArrayList<HashMap<String, String>> {
        val db = this.writableDatabase
        val list = ArrayList<HashMap<String, String>>()
        var hashmap: HashMap<String?, String?>
        db.isOpen
        try {
            val res = db.rawQuery("SELECT * from latlong ORDER BY id ASC", null)
            if (res.moveToFirst()) {
                do {
//                    MainActivity.connectSocket(
//                        res.getString(res.getColumnIndex(LATDATE)),
//                        res.getString(
//                            res.getColumnIndex(
//                                LATDATETIME
//                            )
//                        ),
//                        res.getString(res.getColumnIndex(LAT)),
//                        res.getString(res.getColumnIndex(LON)),
//                        CommonData.deviceID(mcontext),
//                        res.getString(res.getColumnIndex(KEY_ID))
//                    )
                } while (res.moveToNext())
            }
            res.close()
        } finally {
            db.close()
        }
        return list
    }

    /**
     * RK CREATED
     * Clearing LatLong Data
     */
    fun deletelatlong() {
        val db = this.writableDatabase
        db.execSQL("delete from " + TABLE_LATLONG)
        db.close()
    }

    /**
     * RK CREATED
     * Getting Lead Titles
     */
    @SuppressLint("Range")
    fun getTitle_Lead(notifDate: String): HashMap<String, String> {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT * from CalenderEvents Where " + EV_NOTIF_BEFORE + "=?",
            arrayOf(notifDate.toString())
        )
        val titles = HashMap<String, String>()
        if (res.moveToFirst()) {
            do {
                titles["EV_TITLE"] = res.getString(res.getColumnIndex(EV_TITLE))
                titles["EV_LEAD"] = res.getString(res.getColumnIndex(EV_RECIPIENTS))
                titles["EV_TIME"] = res.getString(res.getColumnIndex(EV_TIME))
                titles["EV_DATE"] = res.getString(res.getColumnIndex(EV_DATE))
                titles["EV_DESCRIPTION"] = res.getString(res.getColumnIndex(EV_DESCRIPTION))
            } while (res.moveToNext())
        }
        res.close()
        return titles
    }

    /**
     * RK CREATED
     * Updating Event Notifications
     */
    fun updateEvent_Notif(
        TitleOfMeet: String, LeadOfMeet: String, Meeting_Time: String,
        Meeting_Date: String, NEWTIME: String?
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("eventDate", "updateEvent_Notif")
        values.put("eventNotifBefore", NEWTIME)
        db.update(
            TABLE_CAL_EVENTS, values,
            EV_DATE + " = ? AND " + EV_TITLE + " = ? AND " + EV_TIME + " = ? AND " + EV_RECIPIENTS + " = ?",
            arrayOf(Meeting_Date, TitleOfMeet, Meeting_Time, LeadOfMeet)
        )
        db.close()
    }

    /**
     * RK CREATED
     * Todays Meetings List
     */
    @SuppressLint("Range")
    fun getTodaysMeetings(Date: String): ArrayList<HashMap<String, String>> {
        val finalList = ArrayList<HashMap<String, String>>()
        val meeting = HashMap<String, String>()
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT * from CalenderEvents Where " + EV_DATE + "=?",
            arrayOf(Date.toString())
        )
        if (res.moveToFirst()) {
            do {
                meeting["TITLE"] = res.getString(res.getColumnIndex(EV_TITLE))
                meeting["DATE"] = res.getString(res.getColumnIndex(EV_DATE))
                meeting["TIME"] = res.getString(res.getColumnIndex(EV_TIME))
                meeting["STATUS"] = res.getString(res.getColumnIndex(EV_STATUS))
                meeting["LEAD"] = res.getString(res.getColumnIndex(EV_RECIPIENTS))
                meeting["MEET_ID_SERVER"] = res.getString(res.getColumnIndex(EV_UID))
                Log.d("meeting Details", "Hash map===$meeting")

                finalList.add(meeting)
            } while (res.moveToNext())
        }
        Log.d("meeting Details list", "List of meeting details===$finalList")
        res.close()
        return finalList
    }

    /**
     * RK CREATED
     * Meetings List For CheckIn
     */
    @SuppressLint("Range")
    fun getMeetingForCheckIn(date: String, lat: String, lon: String): Int {
        val lat_checkIn: Float
        val long_checkIn: Float
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT * from CalenderEvents Where " + EV_DATE + "=?",
            arrayOf(date.toString())
        )
        val mySharedPreferences =
            mcontext.getSharedPreferences("MyPref_lat_long", Context.MODE_PRIVATE)
        lat_checkIn = mySharedPreferences.getFloat("Latitude", 0f)
        long_checkIn = mySharedPreferences.getFloat("Longitude", 0f)
        var count = 0
        if (res.moveToFirst()) {
            do {
                count = count + 1
                val lati = res.getString(res.getColumnIndex(EV_LAT))
                val loni = res.getString(res.getColumnIndex(EV_LONG))
                val Title = res.getString(res.getColumnIndex(EV_TITLE))
                var status = res.getString(res.getColumnIndex(EV_CHECKIN_STATUS))
                val month = res.getString(res.getColumnIndex(EV_DATE_MONTH))
                val day = res.getString(res.getColumnIndex(EV_DATE_DAY))
                val year = res.getString(res.getColumnIndex(EV_DATE_YEAR))

                if (status == null) {
                    status = "false"
                }
                try {
                    if (status != "True") {
                        if (lati != "" && loni != "") {
                            val lat2 = lati.toDouble()
                            val lon2 = loni.toDouble()
                            val dist = distance(lat.toDouble(), lon.toDouble(), lat2, lon2, "K")
                            //							Toast.makeText(mcontext, "Check in Distance =" + dist, Toast.LENGTH_SHORT).show();
                            if (dist < 1) {
                                if (lat_checkIn == 0.0f && long_checkIn == 0.0f) {
//								Toast.makeText(mcontext, "Checkout =" + dist, Toast.LENGTH_SHORT).show();
                                    val dialog = Dialog(mcontext)
                                    dialog.setContentView(R.layout.checkinalertdialog)
                                    dialog.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
                                    // Set dialog title
                                    dialog.setTitle("INSIGHT")
                                    //								MyService.sysAlertCount="Title";
                                    // set values for custom dialog components - text, image and button
                                    val checkin =
                                        dialog.findViewById<View>(R.id.checkin) as TextView
                                    val text =
                                        dialog.findViewById<View>(R.id.meetingtiltle) as TextView
                                    text.text = "Please check in to start the meeting"
                                    dialog.show()

                                    //									MyService.sysAlertCount = 1;
                                    val monthname = DateFormat.format("MMMM", Date()) as String
                                    checkin.setOnClickListener { //										MyService.sysAlertCount=null;
                                        val check =
                                            Intent(mcontext, ShowEventDetailsActivity::class.java)
                                        check.putExtra("date", "$day-$monthname-$year")
                                        check.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        mcontext.startActivity(check)
                                        dialog.dismiss()
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } while (res.moveToNext())
        }
        res.close()
        return count
    }

    val profilesCount: Int
        /**
         * RK CREATED
         * GETTING COUNT OF LAT LONG
         */
        get() {
            val countQuery = "SELECT  * FROM  latlong"
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            val cnt = cursor.count
            cursor.close()
            return cnt
        }

    /**::::::::::::::::::::::::::::::::END:::::::::::::::::::::::::::::::::::::: */

    companion object {
        private const val LOG = "DatabaseHelper"

        // Database Version
        private const val DATABASE_VERSION = 2

        // Database Name
        private const val DATABASE_NAME = "CalenderEvents"

        // Table Names
        private const val TABLE_CAL_EVENTS = "CalenderEvents"
        private const val TABLE_ALL_LEADS = "all_lead_list"
        private const val TABLE_CHECKIN = "check_in_list"
        private const val TABLE_ALL_COMPANIES = "all_companies_list"

        // Column names for Calender Events
        private const val KEY_ID = "id"
        private const val EV_DATE = "eventDate"
        private const val EV_DATE_DAY = "eventDate_day"
        private const val EV_DATE_MONTH = "eventDate_month"
        private const val EV_DATE_YEAR = "eventDate_year"
        private const val EV_TIME = "eventTime"
        private const val EV_TITLE = "eventTitle"
        private const val EV_RECIPIENTS = "eventRecipient"
        private const val EV_DESCRIPTION = "eventDescription"
        private const val EV_CONTACT_NO = "eventContactNo"
        private const val EV_EMAIL = "eventEmail"
        private const val EV_NOTIF_BEFORE = "eventNotifBefore"
        private const val EV_PLACE = "eventLocation"
        private const val EV_LAT = "eventLocation_lat"
        private const val EV_LONG = "eventLocation_long"
        private const val EV_COMPANY = "companyName"
        private const val EV_STATUS = "meeting_satatus"
        private const val EV_FLAG = "meeting_flag_number"
        private const val EV_CHECKIN_STATUS = "meeting_checkIN_stats"
        private const val EV_OUTCOMECALL = "event_outcome_call"
        private const val EV_PROJECTVALUE = "event_project_value"
        private const val EV_DISCUSSION_HIGHLIGHTS = "event_disscusion_highlights"
        private const val EV_UID = "server_uid"

        // Column names for Lead List
        private const val LEAD_KEY_ID = "lead_id"
        private const val LEAD_Name = "lead_name"
        private const val LEAD_MOBILE = "lead_mobile"
        private const val LEAD_EMAIL = "lead_email"
        private const val LEAD_ALTMOBILE = "lead_altername_mobile"
        private const val LEAD_COMPANY = "lead_company"
        private const val LEAD_OFFICE_ADD = "lead_office_address"
        private const val LEAD_PURPOSE = "lead_purpose_call"
        private const val LEAD_EXPECTED_DATE = "lead_expecte_date"
        private const val LEAD_FLAG = "lead_flag_number"
        private const val LEAD_UID = "uid"

        // Column names for Lat
        private const val TABLE_LATLONG = "latlong"
        private const val LATDATE = "latdate"
        private const val LATDATETIME = "latdatetime"
        private const val LATDEVICE = "latdevice"
        private const val LAT = "lat"
        private const val LON = "long"

        // Column names for checkIn
        private const val MEETING_ID = "meeting_id"
        private const val MEETING_MMSTYPE = "meeting_mms_type"
        private const val MEETING_DATA = "meeting_data"
        private const val MEETING_FLAG = "meeting_flag_number"
        private const val MEETING_EXTENSION = "meeting_extension"
        private const val MEETING_STATUS = "meeting_status"
        private const val MEETING_LEAD_NAME = "meeting_lead_name"


        private const val COMPANY_ID = "id"
        private const val COMPANY_NAME = "company_name"


        /**::::::::::::::::::::::::::::::::START:::::::::::::::::::::::::::::::::::::: */
        /**
         * RK CREATED
         * Create table calender
         */
        private const val CREATE_TABLE_EVENTS = ("CREATE TABLE "
                + TABLE_CAL_EVENTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                EV_DATE + " DATE," +
                EV_DATE_DAY + " INTEGER," +
                EV_DATE_MONTH + " INTEGER," +
                EV_DATE_YEAR + " INTEGER," +
                EV_TIME + " DATETIME," +
                EV_TITLE + " TEXT," +
                EV_RECIPIENTS + " TEXT," +
                EV_DESCRIPTION + " TEXT," +
                EV_CONTACT_NO + " INTEGER," +
                EV_EMAIL + " TEXT," +
                EV_NOTIF_BEFORE + " DATETIME," +
                EV_COMPANY + " TEXT," +
                EV_STATUS + " TEXT," +
                EV_PLACE + " TEXT, " +
                EV_LAT + " TEXT, " +
                EV_LONG + " TEXT, " +
                EV_FLAG + " INTEGER, " +
                EV_UID + " INTEGER, " +
                EV_OUTCOMECALL + " TEXT, " +
                EV_PROJECTVALUE + " TEXT, " +
                EV_DISCUSSION_HIGHLIGHTS + " TEXT, " +
                EV_CHECKIN_STATUS + " TEXT " +
                ");")

        /**
         * RK CREATED
         * Create Table Research
         */
        private const val CREATE_TABLE_LEADS = ("CREATE TABLE "
                + TABLE_ALL_LEADS +
                "(" +
                LEAD_KEY_ID + " INTEGER PRIMARY KEY," +
                LEAD_Name + " TEXT," +
                LEAD_MOBILE + " INTEGER," +
                LEAD_ALTMOBILE + " INTEGER," +
                LEAD_EMAIL + " TEXT," +
                LEAD_COMPANY + " TEXT," +
                LEAD_OFFICE_ADD + " TEXT," +
                LEAD_PURPOSE + " TEXT," +
                LEAD_EXPECTED_DATE + " TEXT," +
                LEAD_FLAG + " INTEGER unique," +
                LEAD_UID + " TEXT" + ");")

        /**
         * RK CREATED
         * Create Table Research
         */
        private const val CREATE_TABLE_COMPANIES = ("CREATE TABLE "
                + TABLE_ALL_COMPANIES +
                "(" +
                COMPANY_ID + " INTEGER PRIMARY KEY," +
                COMPANY_NAME + " TEXT" + ");")

        /**
         * RK CREATED
         * Create table LATLONG
         */
        private const val CREATE_TABLE_LATLONG = ("CREATE TABLE "
                + TABLE_LATLONG + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                LATDATE + " TEXT," +
                LATDATETIME + " TEXT," +
                LATDEVICE + " TEXT," +
                LAT + " TEXT," +
                LON + " TEXT" +
                ");")


        /**
         * Create table CheckIn
         */
        private const val CREATE_TABLE_CHECKIN = ("CREATE TABLE "
                + TABLE_CHECKIN +
                "(" +
                MEETING_ID + " INTEGER ," +
                MEETING_MMSTYPE + " TEXT," +
                MEETING_DATA + " TEXT," +
                MEETING_FLAG + " TEXT ," +
                MEETING_EXTENSION + " TEXT," +
                MEETING_STATUS + " TEXT," +
                MEETING_LEAD_NAME + " TEXT" + ");")

        /**
         * RK CREATED
         * This function return Distance In KM
         */
        private fun distance(
            lat1: Double,
            lon1: Double,
            lat2: Double,
            lon2: Double,
            unit: String
        ): Double {
            val theta = lon1 - lon2
            var dist =
                sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(
                    deg2rad(theta)
                )
            dist = acos(dist)
            dist = rad2deg(dist)
            dist = dist * 60 * 1.1515
            if (unit === "K") {
                dist = dist * 1.609344
            } else if (unit === "N") {
                dist = dist * 0.8684
            }
            return (dist)
        }

        /**
         * RK CREATED
         * This function converts decimal degrees to radians
         */
        private fun deg2rad(deg: Double): Double {
            return (deg * Math.PI / 180.0)
        }

        /**
         * RK CREATED
         * This function converts radians to decimal degrees
         */
        private fun rad2deg(rad: Double): Double {
            return (rad * 180 / Math.PI)
        }
    }
}
