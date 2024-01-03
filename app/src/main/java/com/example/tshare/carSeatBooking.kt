package com.example.tshare

import java.io.Serializable

class carSeatBooking(private var From: String? = null,
                     private var To:String? = null,
                     private var Date:String? = null,
                     private var Time:String? = null,
                     private var Preference:String? = null,
                     private var Privacy:String? = null,
                     private var timeZone:String? = null,
                     private var userId:String? = null
) : Serializable{
    fun getFrom(): String? {
        return From
    }

    // Setter for username
    fun setFrom(newFrom: String?) {
        From = newFrom
    }
    fun getTo(): String? {
        return To
    }

    // Setter for username
    fun setTo(newTo: String?) {
        To = newTo
    }
    fun getDate(): String? {
        return Date
    }

    // Setter for username
    fun setDate(newDate: String?) {
        Date = newDate
    }
    fun getTime(): String? {
        return Time
    }

    // Setter for username
    fun settime(newTime: String?) {
        Time = newTime
    }
    fun getPreference(): String? {
        return Preference
    }

    // Setter for username
    fun setPreference(newPreference: String?) {
        Preference = newPreference
    }
    fun getPrivacy(): String? {
        return Privacy
    }

    // Setter for username
    fun setPrivacy(newPrivacy: String?) {
        Privacy = newPrivacy
    }
    fun getTimeZone(): String? {
        return timeZone
    }

    // Setter for username
    fun setTimeZone(newTimeZone: String?) {
        timeZone = newTimeZone
    }
    fun getUserId(): String? {
        return userId
    }

    // Setter for username
    fun setUserId(newUserId: String?) {
        userId = newUserId
    }

}