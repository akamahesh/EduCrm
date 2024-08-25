package com.crm.edu.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.Settings
import android.text.format.DateFormat
import android.util.Log
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


object CallLogUtils {
    fun readInboxConversations(context: Context): List<String> = readConversations(context, INBOX)
    fun readSentConversations(context: Context): List<String> = readConversations(context, SENT)
    fun readDraftConversations(context: Context): List<String> = readConversations(context, DRAFT)

    fun readContacts(context: Context): List<String> {
        val messages = mutableListOf<String>()

        val cur: Cursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )!!

        if (cur.count > 0) {
            while (cur.moveToNext()) {
                val id = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name =
                    cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))

                if (cur.getInt(cur.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur = context.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )!!
                    while (pCur.moveToNext()) {
                        val phoneNo =
                            pCur.getString(pCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))

                        messages.add("$name -> $phoneNo")
                    }
                    pCur.close()
                }
            }
        }
        cur.close()

        return messages
    }

    fun readCalls(lastSuccessCallDataSyncCallId:Int?, context: Context, selectedValue:String): List<CallLogInfo> {

        // Get the start of the day
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfStartDay = calendar.time.time

        // Get the current time in milliseconds
        val currentTime = System.currentTimeMillis()
        var endTime = currentTime
        var fromLastDays = 1

        if(selectedValue=="Today"){
            endTime = currentTime
            fromLastDays = 0
        }else if(selectedValue=="Yesterday"){
            endTime = startOfStartDay
            fromLastDays = 1
        }else if (selectedValue=="Week"){
            endTime = currentTime
            fromLastDays = 6
        }




        val phoneNumberUtil =  PhoneNumberUtil.createInstance(context)

        val callLogInfoList = mutableListOf<CallLogInfo>()



       // Set the time range to the last two days
        val startTime = startOfStartDay - (fromLastDays * 24 * 60 * 60 * 1000)




        //val allCalls = Uri.parse("content://call_log/calls")

        val selection = "${CallLog.Calls._ID} > ? AND ${CallLog.Calls.DATE} > ?  AND ${CallLog.Calls.DATE} < ?  "
        val selectionArgs = arrayOf((lastSuccessCallDataSyncCallId?:0).toString(), startTime.toString(), endTime.toString())
        val sortOrder = "${CallLog.Calls.DATE} DESC"
        val projection = arrayOf(
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.TYPE,
            CallLog.Calls.CACHED_NUMBER_LABEL,
            CallLog.Calls.CACHED_NUMBER_TYPE,
            CallLog.Calls.IS_READ,
            CallLog.Calls.NEW,
            CallLog.Calls.GEOCODED_LOCATION,
            CallLog.Calls.VOICEMAIL_URI,
            CallLog.Calls.FEATURES
        )

        val cursor: Cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            selection,
            selectionArgs,
            sortOrder
        )!!


        if (cursor.count > 0) {
            while (cursor.moveToNext()) {

                val id = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls._ID))// For Id

                val number =
                    cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER)) // for  number

                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME)) // for name

                val duration =
                    cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION)) // for duration

                val type = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE))
                    .toInt() // for call type, Incoming or out going.

                val date = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))

                Log.d("dheeresh", "======> $number -> $name -> $duration -> $type )}")


                val numberWithoutIndiaCountryCode = if (number.length>10) {
                    number.substring(number.length- 10)
                } else {
                    number
                }

                Log.d("dheeresh ", "a id :: ${Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)}" )
                val timeInMS = date.toLong()
                val formattedStartDate =
                    DateFormat.format("yyyy-MM-dd HH:mm:ss", Date(timeInMS)).toString()

                val durationNumber = duration.toLongOrNull()
                val formattedEndDate = if (durationNumber != null) {
                    DateFormat.format(
                        "yyyy-MM-dd HH:mm:ss",
                        Date(timeInMS + (duration.toLong() * 1000))
                    )
                        .toString()
                } else {
                    null
                }

                val callTypeString = when (type) {
                    CallLog.Calls.INCOMING_TYPE -> if (durationNumber != null && durationNumber > 0) "Answered" else "Missed"
                    CallLog.Calls.OUTGOING_TYPE -> if (durationNumber != null && durationNumber > 0) "Answered" else "NotPicked"
                    CallLog.Calls.MISSED_TYPE -> "Missed"
                    CallLog.Calls.VOICEMAIL_TYPE -> "Voicemail"
                    CallLog.Calls.REJECTED_TYPE -> "Rejected"
                    CallLog.Calls.BLOCKED_TYPE -> "Blocked"
                    CallLog.Calls.ANSWERED_EXTERNALLY_TYPE -> "Externally Answered"
                    else -> "NA"
                }

                callLogInfoList.add(
                    CallLogInfo(
                        id,
                        name,
                        numberWithoutIndiaCountryCode,
                        duration,
                        type,
                        if (type == CallLog.Calls.OUTGOING_TYPE) CallLog.Calls.OUTGOING_TYPE.toString() else CallLog.Calls.INCOMING_TYPE.toString(),
                        callTypeString,
                        timeInMS,
                        formattedStartDate,
                        formattedEndDate
                    )
                )

                Log.d("dheeresh", "callLogInfoList -> ${callLogInfoList[callLogInfoList.size-1]}")
            }
        }

        cursor.close()

        return callLogInfoList
    }

    fun covertToMobileNumber(number: String?, countryCode: String, phoneNumberUtil: PhoneNumberUtil): String? {
        var mobileNumber: String? = null
        try {
            val isoCode = phoneNumberUtil.getRegionCodeForCountryCode(countryCode.toInt())
            val phoneNumber = phoneNumberUtil.parse(number, isoCode)
            var isValid = phoneNumberUtil.isValidNumber(phoneNumber)
            val type = phoneNumberUtil.getNumberType(phoneNumber)
            isValid =
                isValid && (type == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE || type == PhoneNumberUtil.PhoneNumberType.MOBILE)

            if (isValid) {
                mobileNumber =
                    phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
            } else {
                throw Exception("$number is not a valid mobile number")
            }
        } catch (e: Exception) {
            Log.e( "dheeresh",e.message?:"Exception :covertToMobileNumber" )

        }
        Log.d( "dheeresh","Number: $mobileNumber, Country Code: $countryCode" )
        return mobileNumber
    }

    private fun readConversations(context: Context, scope: String): List<String> {

        val messages = mutableListOf<String>()

        val cursor: Cursor? =
            context.contentResolver.query(Uri.parse(scope), null, null, null, null)

        cursor ?: throw NullPointerException("cursor")

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                var msgData = ""
                for (idx in 0 until cursor.columnCount) {
                    msgData += " " + cursor.getColumnName(idx).toString() + ":" + cursor.getString(
                        idx
                    )
                }

                messages.add(msgData)

            } while (cursor.moveToNext())
        } else {
            // empty box, no SMS
        }

        cursor.close()

        return messages
    }


    data class CallStats(var count: Int, var duration: Int, var formattedDuration: String)

    private fun getCallStatsByType(callType: Int, context: Context): CallStats {
        // Get the start of the day
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfDay = calendar.time

        // Prepare the date format for comparison
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val today = dateFormat.format(startOfDay)

        // Query the call log for calls of the specified type received today
        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(CallLog.Calls.DURATION),
            "${CallLog.Calls.DATE} > ? AND ${CallLog.Calls.TYPE} = ?",
            arrayOf(startOfDay.time.toString(), callType.toString()),
            null
        )

        var callCount = 0
        var totalDuration = 0

        cursor?.use {
            while (it.moveToNext()) {
                val callDuration = it.getString(0)

                // Update the call count and total duration
                callCount++
                totalDuration += callDuration.toInt()
            }
        }

        return CallStats(callCount, totalDuration, getFormattedDuration(totalDuration))
    }


    fun getCallStats(context: Context, selectedValue:String): Triple<CallStats, CallStats, CallStats> {
        val cursor = getCallLogCursor(context, selectedValue)

        var receivedCallsStats = CallStats(0, 0, "")
        var outgoingCallsStats = CallStats(0, 0, "")
        var missedCallsStats = CallStats(0, 0, "")

        cursor?.use {
            val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)
            val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)

            while (it.moveToNext()) {
                val phoneNumber = it.getString(numberIndex)
                val callDuration = it.getString(durationIndex)
                val callType = it.getInt(typeIndex)

                when (callType) {
                    CallLog.Calls.INCOMING_TYPE -> {
                        receivedCallsStats.count++
                        receivedCallsStats.duration += callDuration.toInt()
                    }
                    CallLog.Calls.OUTGOING_TYPE -> {
                        outgoingCallsStats.count++
                        outgoingCallsStats.duration += callDuration.toInt()
                    }
                    CallLog.Calls.MISSED_TYPE -> {
                        missedCallsStats.count++
                        missedCallsStats.duration += callDuration.toInt()
                    }
                }
            }
        }

        receivedCallsStats.formattedDuration = getFormattedDuration(receivedCallsStats.duration)
        outgoingCallsStats.formattedDuration = getFormattedDuration(outgoingCallsStats.duration)
        missedCallsStats.formattedDuration = getFormattedDuration(missedCallsStats.duration)


        return Triple(receivedCallsStats, outgoingCallsStats, missedCallsStats)
    }

    fun getFormattedDuration(duration: Int): String {
        val hours = duration / 3600
        val minutes = (duration % 3600) / 60
        val seconds = duration % 60

        val formattedDuration = StringBuilder()

        if (hours > 0) {
            formattedDuration.append(hours).append("h ")
        }

        if (minutes > 0) {
            formattedDuration.append(minutes).append("m ")
        }

        if (seconds > 0 || formattedDuration.isEmpty()) {
            formattedDuration.append(seconds).append("s")
        }

        return formattedDuration.toString().trim()
    }

    private fun getCallLogCursor(context: Context,selectedValue:String): Cursor? {

        /*
        var tillDay = 0
        var fromLastDays = 1
        if(selectedValue=="Today"){
            tillDay = 0
            fromLastDays = 0
        }else if(selectedValue=="Yesterday"){
            tillDay = 1
            fromLastDays = 1
        }else if (selectedValue=="Week"){
            tillDay = 0
            fromLastDays = 6
        }

        // Get the start of the day
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, - fromLastDays)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfStartDay = calendar.time


        // Get the start of the day
        val calendar2 = Calendar.getInstance()
        calendar2.add(Calendar.DATE, - tillDay);
        calendar2.set(Calendar.HOUR_OF_DAY, 24)
        calendar2.set(Calendar.MINUTE, 60)
        calendar2.set(Calendar.SECOND, 60)
        val startOfEndDay = calendar2.time
*/


        // Get the start of the day
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfStartDay = calendar.time.time

        // Get the current time in milliseconds
        val currentTime = System.currentTimeMillis()
        var endTime = currentTime
        var fromLastDays = 1

        if(selectedValue=="Today"){
            endTime = currentTime
            fromLastDays = 0
        }else if(selectedValue=="Yesterday"){
            endTime = startOfStartDay
            fromLastDays = 1
        }else if (selectedValue=="Week"){
            endTime = currentTime
            fromLastDays = 6
        }

        val startTime = startOfStartDay - (fromLastDays * 24 * 60 * 60 * 1000)
        
        // Query the call log for all calls received today
        val selection = "${CallLog.Calls.DATE} > ? AND ${CallLog.Calls.DATE} < ?"
        val selectionArgs = arrayOf( startTime.toString(), endTime.toString())
        val sortOrder = "${CallLog.Calls.DATE} DESC"

        return context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            selection,
            selectionArgs,
            sortOrder
        )
    }


    const val INBOX = "content://sms/inbox";
    const val SENT = "content://sms/sent";
    const val DRAFT = "content://sms/draft";

}


data class CallLogInfo(
    val logTableId: String,
    val name: String?,
    val number: String?,
    val duration: String?,
    val type: Int?,
    val superType: String?,
    val typeString: String?,
    val timeInMS: Long,
    val startDateTime: String?,
    val endDateTime: String?,
)
