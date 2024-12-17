package com.tekskills.st_tekskills.utils.receiver

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.LocationManager
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by RK
 */
class CommonData {
    val time: String?
        get() {
            var dte: String? = null

            val date = Date()
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            dte = formatter.format(date)

            Log.v("## SignUp Date", "## SignUp Date : $dte")
            return dte
        }


    companion object {
        const val EMPTY: String = ""
        const val USERNAME: String = "customer_name"

        //public static final String DEVICE_ID = "device_id";
        const val CUSTOMER_ID: String = "customer_id"
        const val MOBILE_NUMBER: String = "mobile_number"
        const val EMAIL: String = "email"
        const val UID: String = "UID"
        const val TIME: String = "0:00"
        var CheckinStatus: Boolean = false
        private val TYPE_FACES: MutableMap<String, Typeface?> = HashMap()
        const val PLACESAPI: String = "https://maps.googleapis.com/maps/api/geocode/json?address="
        const val PLACESAPIKEY: String = "&key=AIzaSyAmtCl_1MQ9Uw0XpKPE54-oQCCv9rWWVPo"


        // * font style of sourceScansPro  *//
        const val FONT_SOURCESANSPRO_LIGHT: String = "fonts/SourceSansPro-Light.ttf"
        const val FONT_SOURCESANSPRO_SEMIBOLD: String = "fonts/SourceSansPro-Semibold.ttf"
        const val FONT_SOURCESANSPRO_REGULAR: String = "fonts/SourceSansPro-Regular.ttf"

        // * font style of oswald and Sharp sans*//
        const val FONT_OSWALD_BOLD: String = "fonts/Oswald-Bold.ttf"
        const val FONT_SHARP_SANS: String = "fonts/Oswald-Bold.ttf"

        // * font style of AVENIR font  *//
        const val FONT_AVENIR_BOOK: String = "fonts/AvenirLTStd-Book.otf"
        const val FONT_AVENIR_BOLD: String = "fonts/AvenirNextLTPro-Bold.otf"
        const val FONT_AVENIR_MEDIUM: String = "fonts/AvenirLTStd-Medium.otf"
        const val FONT_AVENIR_HEAVY: String = "fonts/AvenirLTStd-Heavy.otf"
        const val FONT_AVENIR_LIGHT: String = "fonts/AvenirLTStd-Light.otf"

        // * font style of Lato  *//
        const val FONT_LATO_LIGHT: String = "fonts/Lato-Light.ttf"
        const val FONT_LATO_BOLD: String = "fonts/Lato-Bold.ttf"
        const val FONT_LATO_BLACK: String = "fonts/Lato-Black.ttf"
        const val FONT_LATO_REGULAR: String = "fonts/Lato-Regular.ttf"

        // * font style of Roboto  *//
        const val FONT_ROBOTO_LIGHT: String = "fonts/Roboto-Light.ttf"
        const val FONT_ROBOTO_BOLD: String = "fonts/Roboto-Bold.ttf"
        const val FONT_ROBOTO_MEDIUM: String = "fonts/Roboto-Medium.ttf"
        const val FONT_ROBOTO_REGULAR: String = "fonts/Roboto-Regular.ttf"


        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        fun deviceID(context: Context): String? {
            val TelephonyMgr =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null
            }
            val m_deviceId = TelephonyMgr.deviceId
            //Toast.makeText(context, "DEVICE ID"+m_deviceId, Toast.LENGTH_SHORT).show();
            Log.v("DEVICEID : ", m_deviceId)
            return m_deviceId
        }

        fun checkLocationPermission(context: Context?): Boolean {
            val result = ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            return (result == PackageManager.PERMISSION_GRANTED)
        }


        fun closeKeyBoard(context: Context, view: View) {
            try {
                val `in` =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                `in`.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun isLocationServiceEnabled(lm: LocationManager): Boolean {
            var gps_enabled = false
            var network_enabled = false
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return gps_enabled || network_enabled
        }

        fun getFontTypeface(context: Context, fontFileName: String): Typeface? {
            var typeFace = TYPE_FACES[fontFileName]

            if (typeFace == null) {
                typeFace = Typeface.createFromAsset(context.resources.assets, fontFileName)
                TYPE_FACES[fontFileName] = typeFace
            }
            return typeFace
        }


        val yearFormateDate: String?
            get() {
                var dte: String? = null

                val date = Date()
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                dte = formatter.format(date)

                Log.v("## SignUp Date", "## SignUp Date : $dte")
                return dte
            }

        fun getYearMonthDayFormat(dateString: String?): String {
            if (dateString == null || dateString.trim { it <= ' ' } == EMPTY) return EMPTY

            val fmt = SimpleDateFormat("yyyy/MM/dd_HHmmss")
            var date: Date? = null
            try {
                date = fmt.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val fmtOut = SimpleDateFormat("dd MMM yyyy")
            return fmt.format(date)
        }

        val dateTime: String
            get() {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                // Date currentTime = Calendar.getInstance().getTime();
                val currentDateandTime = sdf.format(Date())
                return (currentDateandTime)
            }
    }
}
