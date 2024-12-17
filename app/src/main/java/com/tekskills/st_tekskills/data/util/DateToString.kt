package com.tekskills.st_tekskills.data.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import com.tekskills.st_tekskills.R
import com.tekskills.st_tekskills.utils.UtilsConstants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


class DateToString {
    @SuppressLint("SimpleDateFormat")
    companion object {
        fun convertDateToString(date: Date): String {
            val format1 = "yyyy-MM-dd"
            val format2 = "yyyy-MM-dd'T'HH:mm:ss"
            val dateInfinity = Date(Constants.MAX_TIMESTAMP)
            return if (dateInfinity.compareTo(date) == 0) "N/A"
            else if (date.seconds == 0) {
                val df = SimpleDateFormat(format1)
                df.format(date)
            } else {
                val df = SimpleDateFormat(format1)
                df.format(date)
            }
        }

        fun convertDateTimeToString(date: Date): String {
//            val format1 = "yyyy-MM-dd"
            val format1 = "yyyy-MM-dd'T'HH:mm:ss.S"
            val dateInfinity = Date(Constants.MAX_TIMESTAMP)
            return if (dateInfinity.compareTo(date) == 0) "N/A"
            else if (date.seconds == 0) {
                val df = SimpleDateFormat(format1)
                df.format(date)
            } else {
                val df = SimpleDateFormat(format1)
                df.format(date)
            }
        }

        fun convertDateToStringmain() {
            val inputDateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val inputDateString = "2024-04-01T00:00:00.000+00:00"
            val date = inputDateFormat.parse(inputDateString)

            val outputDateString = outputDateFormat.format(date)
            println(outputDateString) // Output: 2024-04-01
        }

        fun travelDistanceAmount(distanceInKm: Double): String {
            val price: Double =
                (distanceInKm!! * UtilsConstants.Transportation.UnitPrice.bikeType) as Double

            return String.format("%.2f", price) + ""
        }

        fun travelDistanceKms(distanceInKm: Double): String {
            return String.format("%.2fkms", distanceInKm)
        }

        fun amountConverter(distanceInKm: Double): String {
            return String.format("%.2f", distanceInKm)
        }

        fun convertDateToStringDateWise(date: Date): String {
            val format1 = "MMM dd, yyyy"
//            val format2 = "MMM dd, yyyy, hh:mm a"
            val dateInfinity = Date(Constants.MAX_TIMESTAMP)
            return if (dateInfinity.compareTo(date) == 0) "N/A"
            else if (date.seconds == 0) {
                val df = SimpleDateFormat(format1)
                df.format(date)
            } else {
                val df = SimpleDateFormat(format1)
                df.format(date)
            }
        }

        fun convertDateToStringDateTimeWise(date: Date): String {
//            val format1 = "MMM dd, yyyy"
            val format2 = "MMM dd, yyyy, hh:mm a"
            val dateInfinity = Date(Constants.MAX_TIMESTAMP)
            return if (dateInfinity.compareTo(date) == 0) "N/A"
            else if (date.seconds == 0) {
                val df = SimpleDateFormat(format2)
                df.format(date)
            } else {
                val df = SimpleDateFormat(format2)
                df.format(date)
            }
        }

        fun convertStringToDate(dateString: String): Date? {
            val format1 = SimpleDateFormat("yyyy-MM-dd")
            val format2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

            try {
                // Try to parse the date string using the first format
                return format1.parse(dateString)
            } catch (e1: Exception) {
                try {
                    // If parsing using the first format fails, try the second format
                    return format2.parse(dateString)
                } catch (e2: Exception) {
                    // Handle the case where parsing using both formats fails
                    return null
                }
            }
        }

        fun getTimeAgo(timestampDate: String): String {
            val currentTimeMillis = System.currentTimeMillis()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val dateTime = LocalDateTime.parse(timestampDate, formatter)

            // Convert LocalDateTime to a timestamp (milliseconds since epoch)
            val timestamp = dateTime.toEpochSecond(java.time.ZoneOffset.UTC) * 1000

            val timeDifferenceMillis = currentTimeMillis - timestamp

//            val resources = context.resources

            val justNowThresholdMillis = 10000 // 10 seconds
            val minuteMillis = 60 * 1000
            val hourMillis = 60 * minuteMillis
            val dayMillis = 24 * hourMillis

            return when {
                timeDifferenceMillis < justNowThresholdMillis -> {
                    "Just Now"
//                    resources.getString(R.string.just_now)
                }

                timeDifferenceMillis < minuteMillis -> {
                    val secondsAgo = timeDifferenceMillis / 1000
                    "${secondsAgo.toInt()} seconds Ago"
//                    resources.getQuantityString(R.plurals.seconds_ago, secondsAgo.toInt(), secondsAgo)
                }

                timeDifferenceMillis < hourMillis -> {
                    val minutesAgo = timeDifferenceMillis / minuteMillis
                    "${minutesAgo.toInt()} minutes Ago"
//                    resources.getQuantityString(R.plurals.minutes_ago, minutesAgo.toInt(), minutesAgo)
                }

                timeDifferenceMillis < dayMillis -> {
                    val hoursAgo = timeDifferenceMillis / hourMillis
                    "${hoursAgo.toInt()} hours Ago"
//                    resources.getQuantityString(R.plurals.hours_ago, hoursAgo.toInt(), hoursAgo)
                }

                else -> {
                    val daysAgo = timeDifferenceMillis / dayMillis
//                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                sdf.timeZone = TimeZone.getDefault()
//                val formattedDate = sdf.format(Date(timestamp))
                    "${daysAgo.toInt()} days Ago"
//                    resources.getQuantityString(R.plurals.days_ago, daysAgo.toInt(),daysAgo)
                }
            }
        }

        fun convertDateStringToCustomFormat(dateString: String?): String {
            if (dateString.isNullOrEmpty()) return ""

            val inputFormats = listOf(
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd"
            )

            val outputFormatWithTime = "MMM dd, yyyy, hh:mm a"
            val outputFormatDateOnly = "MMM dd, yyyy"

            for (inputFormat in inputFormats) {
                try {
                    val inputFormatter = SimpleDateFormat(inputFormat, Locale.US)
                    val parsedDate = inputFormatter.parse(dateString)

                    if (inputFormat == "yyyy-MM-dd") {
                        val outputFormatter = SimpleDateFormat(outputFormatDateOnly, Locale.US)
                        return outputFormatter.format(parsedDate)
                    } else {
                        val outputFormatter = SimpleDateFormat(outputFormatWithTime, Locale.US)
                        return outputFormatter.format(parsedDate)
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "Exception occurred: ${e.toString()}")
                }
            }

            return ""
        }

        fun isCurrentDate(dateString: String): Boolean {
            val inputFormats = listOf(
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd"
            )

            val currentDate = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            for (format in inputFormats) {
                val sdf = SimpleDateFormat(format, Locale.getDefault())
                try {
                    val parsedDate = sdf.parse(dateString)
                    if (parsedDate != null) {
                        val parsedCalendar = Calendar.getInstance().apply {
                            time = parsedDate
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        if (parsedCalendar.time == currentDate) {
                            return true
                        }
                    }
                } catch (e: ParseException) {
                    // Continue to the next format if parsing fails
                }
            }
            return false
        }

        fun isCurrentTimeRange(dateString: String): Boolean {
            val inputFormats = listOf(
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd"
            )

            val currentTime = Calendar.getInstance().time

            for (format in inputFormats) {
                val sdf = SimpleDateFormat(format, Locale.getDefault())
                try {
                    val parsedDate = sdf.parse(dateString)
                    if (parsedDate != null) {
                        val parsedCalendar = Calendar.getInstance().apply {
                            time = parsedDate
                        }

                        // Calculate the range: 30 minutes before and 5 minutes after
                        val startRange = Calendar.getInstance().apply {
                            time = parsedDate
                            add(Calendar.MINUTE, -30)
                        }.time

                        val endRange = Calendar.getInstance().apply {
                            time = parsedDate
                            add(Calendar.MINUTE, 30)
                        }.time

                        // Check if the current time is within the range
                        if (currentTime in startRange..endRange) {
                            return true
                        }
                    }
                } catch (e: ParseException) {
                    // Continue to the next format if parsing fails
                }
            }
            return false
        }

        fun isDateWithin3Hours(inputDate: String): Boolean {
            val inputFormats = listOf(
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd"
            )

            val currentDate = Date()
            val calendar = Calendar.getInstance()

            // Create a list to store parsed dates
            val parsedDates = mutableListOf<Date?>()

            // Parse the input date with each format until one succeeds
            for (format in inputFormats) {
                val dateFormat = SimpleDateFormat(format, Locale.getDefault())
                try {
                    val parsedDate = dateFormat.parse(inputDate)
                    if (parsedDate != null) {
                        parsedDates.add(parsedDate)
                    }
                } catch (e: Exception) {
                    // Ignore parse exceptions and try the next format
                }
            }

            // If no valid date could be parsed, return false
            if (parsedDates.isEmpty()) {
                return false
            }

            // Take the first successfully parsed date
            val parsedDate = parsedDates.first()!!

            // Get time 3 hours before and after current date
            calendar.time = currentDate
            calendar.add(Calendar.HOUR_OF_DAY, -3)
            val threeHoursBefore = calendar.time

            calendar.time = currentDate
            calendar.add(Calendar.HOUR_OF_DAY, 3)
            val threeHoursAfter = calendar.time

            // Check if parsed date is within 3 hours range
            return parsedDate.after(threeHoursBefore) && parsedDate.before(threeHoursAfter)
        }



//        fun convertDateStringToCustomFormat(dateString: String?): String {
//            if (dateString.isNullOrEmpty()) return ""
//            val inputFormats = listOf(
//                "yyyy-MM-dd",
//                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
//                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
//                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
//                "yyyy-MM-dd'T'HH:mm:ss.SSS",
//                "yyyy-MM-dd'T'HH:mm:ss",
//            )
//
//            val outputFormats = listOf(
//                "MMM dd, yyyy, hh:mm a",
//                "MMM dd, yyyy",
//            )
//
//            for (inputFormat in inputFormats) {
//                try {
//                    val inputFormatter = SimpleDateFormat(inputFormat, Locale.US)
//                    val parsedDate = inputFormatter.parse(dateString)
//
//                    for (outputFormat in outputFormats) {
//                        try {
//                            val outputFormatter = SimpleDateFormat(outputFormat, Locale.US)
//                            return outputFormatter.format(parsedDate)
//                        } catch (e2: Exception) {
//                            Log.d("TAG", "exception at time formatter ${e2.toString()}")
//                        }
//                    }
//                } catch (e1: Exception) {
//                    Log.d("TAG", "exception at time formatter ${e1.toString()}")
//                }
//            }
//
//            return if (dateString.length >= 10) dateString.substring(0, 10) else ""
//        }

        fun calculateDaysBetweenDates(startDate: String, endDate: String): Long {
            // Parse the date strings into LocalDate objects
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val start = LocalDate.parse(startDate, formatter)
            val end = LocalDate.parse(endDate, formatter)

            // Calculate the difference in days
            return ChronoUnit.DAYS.between(start, end)
        }


        fun convertDateStringToNormalFormat(dateString: String): String {
            val inputFormats = listOf(
                "yyyy-MM-dd",
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            )

            val outputFormats = listOf(
                "yyyy-MM-dd",
            )

            for (inputFormat in inputFormats) {
                try {
                    val inputFormatter = SimpleDateFormat(inputFormat, Locale.US)
                    val parsedDate = inputFormatter.parse(dateString)

                    for (outputFormat in outputFormats) {
                        try {
                            val outputFormatter = SimpleDateFormat(outputFormat, Locale.US)
                            return outputFormatter.format(parsedDate)
                        } catch (e2: Exception) {
                            Log.d("TAG", "exception at time formatter ${e2.toString()}")
                        }
                    }
                } catch (e1: Exception) {
                    Log.d("TAG", "exception at time formatter ${e1.toString()}")
                }
            }

            return if (dateString.length >= 10) dateString.substring(0, 10) else ""
        }

        fun getTimeAgo(context: Context, timestampDate: String): String {
            val currentTimeMillis = System.currentTimeMillis()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val dateTime =
                try {
                    LocalDateTime.parse(timestampDate, formatter1)
                } catch (e1: Exception) {
                    try {
                        LocalDateTime.parse(timestampDate, formatter)
                    } catch (e1: Exception) {
                        return ""
                    }
                }

            // Convert LocalDateTime to a timestamp (milliseconds since epoch)
            val timestamp = dateTime.toEpochSecond(java.time.ZoneOffset.UTC) * 1000

            val timeDifferenceMillis = currentTimeMillis - timestamp

            val resources = context.resources

            val justNowThresholdMillis = 10000 // 10 seconds
            val minuteMillis = 60 * 1000
            val hourMillis = 60 * minuteMillis
            val dayMillis = 24 * hourMillis

            return when {
                timeDifferenceMillis < justNowThresholdMillis -> {
                    resources.getString(R.string.just_now)
                }

                timeDifferenceMillis < minuteMillis -> {
                    val secondsAgo = timeDifferenceMillis / 1000
                    resources.getQuantityString(
                        R.plurals.seconds_ago,
                        secondsAgo.toInt(),
                        secondsAgo
                    )
                }

                timeDifferenceMillis < hourMillis -> {
                    val minutesAgo = timeDifferenceMillis / minuteMillis
                    resources.getQuantityString(
                        R.plurals.minutes_ago,
                        minutesAgo.toInt(),
                        minutesAgo
                    )
                }

                timeDifferenceMillis < dayMillis -> {
                    val hoursAgo = timeDifferenceMillis / hourMillis
                    resources.getQuantityString(R.plurals.hours_ago, hoursAgo.toInt(), hoursAgo)
                }

                else -> {
                    val daysAgo = timeDifferenceMillis / dayMillis
//                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                sdf.timeZone = TimeZone.getDefault()
//                val formattedDate = sdf.format(Date(timestamp))
                    resources.getQuantityString(R.plurals.days_ago, daysAgo.toInt(), daysAgo)
                }
            }
        }

        fun getTextviewVisibility(textData: String?): Int {
            return if (textData.isNullOrEmpty()) View.VISIBLE else View.GONE
        }

        fun getCheckINOUTVisibility(textData: String?): Int {
            return if (textData.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        fun checkTextviewNullOrEmpty(textData: String?): Int {
            return if (textData.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        fun setCheckoutVisibility(checkInDate: String?, checkOutDate: String?): Int {
            return if (!checkInDate.isNullOrEmpty() && checkOutDate.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        fun determineStage(managerStatus: String?, financeStatus: String?): String {
            return when {
                managerStatus.isNullOrEmpty() && financeStatus.isNullOrEmpty() -> "Manager Approval Pending"
                managerStatus == "Pending" -> "Manager Approval Pending"
                managerStatus == "Rejected" -> "Manager Rejected"
                managerStatus == "Pending" && financeStatus.isNullOrEmpty() -> "Manager Approval Pending"
                managerStatus == "Rejected" && financeStatus.isNullOrEmpty() -> "Manager Rejected"
                managerStatus == "Approved" && financeStatus.isNullOrEmpty() -> "Finance Team Approval Pending"
                managerStatus == "Approved" && financeStatus == "Pending" -> "Finance Team Approval Pending"
                managerStatus == "Approved" && financeStatus == "Rejected" -> "Finance Team Rejected"
                managerStatus == "Approved" && financeStatus == "Approved" -> "Approved by Finance"
                else -> "Unknown Stage"
            }
        }

        fun setAddMOMVisibility(checkInDate: String?, checkOutDate: String?): Int {
            return if (!checkInDate.isNullOrEmpty() && !checkOutDate.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        fun convertStringToDateformat(dateString: String): Date? {


            val format1 = SimpleDateFormat("yyyy-MM-dd")
            val format2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

            try {
                // Try to parse the date string using the first format
                return format1.parse(dateString)
            } catch (e1: Exception) {
                try {
                    // If parsing using the first format fails, try the second format
                    return format2.parse(dateString)
                } catch (e2: Exception) {
                    // Handle the case where parsing using both formats fails
                    return null
                }
            }
        }
    }


}