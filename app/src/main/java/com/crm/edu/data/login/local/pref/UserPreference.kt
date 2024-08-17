package com.crm.edu.data.login.local.pref

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(context: Context) {

    private val dataStore = context.dataStore

    // Get User Data
    val userData: Flow<Map<String, String?>> = dataStore.data.map { preferences ->
        mapOf(
            UserPreferencesKeys.STAFF_ID.name to preferences[UserPreferencesKeys.STAFF_ID],
            UserPreferencesKeys.EMAIL.name to preferences[UserPreferencesKeys.EMAIL],
            UserPreferencesKeys.FIRST_NAME.name to preferences[UserPreferencesKeys.FIRST_NAME],
            UserPreferencesKeys.LAST_NAME.name to preferences[UserPreferencesKeys.LAST_NAME],
            UserPreferencesKeys.PHONE_NUMBER.name to preferences[UserPreferencesKeys.PHONE_NUMBER],
            UserPreferencesKeys.REPORTING_PERSON.name to preferences[UserPreferencesKeys.REPORTING_PERSON],
            UserPreferencesKeys.PROFILE_IMAGE.name to preferences[UserPreferencesKeys.PROFILE_IMAGE],
            UserPreferencesKeys.LEAD_TYPE.name to preferences[UserPreferencesKeys.LEAD_TYPE],
            UserPreferencesKeys.ACTIVE.name to preferences[UserPreferencesKeys.ACTIVE],
            UserPreferencesKeys.REPORTING_PERSON_NAME.name to preferences[UserPreferencesKeys.REPORTING_PERSON_NAME],
            UserPreferencesKeys.STAFF_NAME.name to preferences[UserPreferencesKeys.STAFF_NAME],
            UserPreferencesKeys.DEPARTMENT_TYPE.name to preferences[UserPreferencesKeys.DEPARTMENT_TYPE],
            UserPreferencesKeys.OFFICE_LOCATION.name to preferences[UserPreferencesKeys.OFFICE_LOCATION],
            UserPreferencesKeys.LATITUDE.name to preferences[UserPreferencesKeys.LATITUDE],
            UserPreferencesKeys.LONGITUDE.name to preferences[UserPreferencesKeys.LONGITUDE],
            UserPreferencesKeys.DESIGNATION.name to preferences[UserPreferencesKeys.DESIGNATION],
            UserPreferencesKeys.CALLS_UPDATES.name to preferences[UserPreferencesKeys.CALLS_UPDATES],
            UserPreferencesKeys.DEPARTMENT_ID.name to preferences[UserPreferencesKeys.DEPARTMENT_ID]
        )
    }

    // Function to get the department ID
    val departmentId: Flow<String?> = dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.DEPARTMENT_ID]
    }

    // Suspend function to get the department ID directly
    suspend fun getDepartmentId(): String? {
        return dataStore.data
            .map { preferences ->
                preferences[UserPreferencesKeys.DEPARTMENT_ID]
            }
            .first()
    }
    // Suspend function to get the reporting ID directly
    suspend fun getReportingId(): String? {
        return dataStore.data
            .map { preferences ->
                preferences[UserPreferencesKeys.REPORTING_PERSON]
            }
            .first()
    }

    // Suspend function to get the reporting ID directly
    suspend fun getJWTtoken(): String? {
        return dataStore.data
            .map { preferences ->
                preferences[UserPreferencesKeys.JWT_TOKEN]
            }
            .first()
    }

    // Save User Data
    suspend fun saveUserData(userData: Map<String, String>) {
        dataStore.edit { preferences ->
            userData.forEach { (key, value) ->
                when (key) {
                    UserPreferencesKeys.STAFF_ID.name -> preferences[UserPreferencesKeys.STAFF_ID] =
                        value

                    UserPreferencesKeys.EMAIL.name -> preferences[UserPreferencesKeys.EMAIL] = value
                    UserPreferencesKeys.FIRST_NAME.name -> preferences[UserPreferencesKeys.FIRST_NAME] =
                        value

                    UserPreferencesKeys.LAST_NAME.name -> preferences[UserPreferencesKeys.LAST_NAME] =
                        value

                    UserPreferencesKeys.PHONE_NUMBER.name -> preferences[UserPreferencesKeys.PHONE_NUMBER] =
                        value

                    UserPreferencesKeys.REPORTING_PERSON.name -> preferences[UserPreferencesKeys.REPORTING_PERSON] =
                        value

                    UserPreferencesKeys.PROFILE_IMAGE.name -> preferences[UserPreferencesKeys.PROFILE_IMAGE] =
                        value

                    UserPreferencesKeys.LEAD_TYPE.name -> preferences[UserPreferencesKeys.LEAD_TYPE] =
                        value

                    UserPreferencesKeys.ACTIVE.name -> preferences[UserPreferencesKeys.ACTIVE] =
                        value

                    UserPreferencesKeys.REPORTING_PERSON_NAME.name -> preferences[UserPreferencesKeys.REPORTING_PERSON_NAME] =
                        value

                    UserPreferencesKeys.STAFF_NAME.name -> preferences[UserPreferencesKeys.STAFF_NAME] =
                        value

                    UserPreferencesKeys.DEPARTMENT_TYPE.name -> preferences[UserPreferencesKeys.DEPARTMENT_TYPE] =
                        value

                    UserPreferencesKeys.OFFICE_LOCATION.name -> preferences[UserPreferencesKeys.OFFICE_LOCATION] =
                        value

                    UserPreferencesKeys.LATITUDE.name -> preferences[UserPreferencesKeys.LATITUDE] =
                        value

                    UserPreferencesKeys.LONGITUDE.name -> preferences[UserPreferencesKeys.LONGITUDE] =
                        value

                    UserPreferencesKeys.DESIGNATION.name -> preferences[UserPreferencesKeys.DESIGNATION] =
                        value

                    UserPreferencesKeys.CALLS_UPDATES.name -> preferences[UserPreferencesKeys.CALLS_UPDATES] =
                        value

                    UserPreferencesKeys.DEPARTMENT_ID.name -> preferences[UserPreferencesKeys.DEPARTMENT_ID] =
                        value

                    UserPreferencesKeys.LOGIN_TOKEN.name -> preferences[UserPreferencesKeys.LOGIN_TOKEN] =
                        value

                    UserPreferencesKeys.JWT_TOKEN.name -> preferences[UserPreferencesKeys.JWT_TOKEN] =
                        value
                }
            }
        }
    }

    suspend fun setUserLoggedIn(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.IS_LOGGED_IN] = loggedIn
        }
    }

    suspend fun isUserLoggedIn(): Boolean {
        val loggedIn = dataStore.data
            .map { preferences ->
                preferences[UserPreferencesKeys.IS_LOGGED_IN]
            }
            .first() ?: false

        Log.d("EduLogs", "User Pref User Logged in :  $loggedIn")
        return loggedIn
    }
}
