package com.crm.edu.data.login.local.pref

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    val STAFF_ID = stringPreferencesKey("staffid")
    val EMAIL = stringPreferencesKey("email")
    val FIRST_NAME = stringPreferencesKey("firstname")
    val LAST_NAME = stringPreferencesKey("lastname")
    val PHONE_NUMBER = stringPreferencesKey("phonenumber")
    val REPORTING_PERSON = stringPreferencesKey("reporting_person")
    val PROFILE_IMAGE = stringPreferencesKey("profile_image")
    val LEAD_TYPE = stringPreferencesKey("lead_type")
    val ACTIVE = stringPreferencesKey("active")
    val REPORTING_PERSON_NAME = stringPreferencesKey("reporting_person_name")
    val STAFF_NAME = stringPreferencesKey("staff_name")
    val DEPARTMENT_TYPE = stringPreferencesKey("department_type")
    val OFFICE_LOCATION = stringPreferencesKey("office_location")
    val LATITUDE = stringPreferencesKey("latitude")
    val LONGITUDE = stringPreferencesKey("longitude")
    val DESIGNATION = stringPreferencesKey("designation")
    val CALLS_UPDATES = stringPreferencesKey("calls_updates")
    val DEPARTMENT = stringPreferencesKey("department")
    val LOGIN_TOKEN = stringPreferencesKey("login_token")
    val JWT_TOKEN = stringPreferencesKey("jwt_token")
}
