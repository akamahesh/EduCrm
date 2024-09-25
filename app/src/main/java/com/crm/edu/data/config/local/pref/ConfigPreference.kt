package com.crm.edu.data.config.local.pref

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "config_prefs")

class ConfigPreferences(context: Context) {

    private val dataStore = context.dataStore


    val configData: Flow<Map<String, String?>> = dataStore.data.map { preferences ->
        mapOf(
            ConfigPreferencesKeys.ID.name to preferences[ConfigPreferencesKeys.ID],
            ConfigPreferencesKeys.COMPANY_NAME.name to preferences[ConfigPreferencesKeys.COMPANY_NAME],
            ConfigPreferencesKeys.LOGO.name to preferences[ConfigPreferencesKeys.LOGO],
            ConfigPreferencesKeys.BASE_URL.name to preferences[ConfigPreferencesKeys.BASE_URL],
            ConfigPreferencesKeys.VERSION.name to preferences[ConfigPreferencesKeys.VERSION],
            ConfigPreferencesKeys.CREATED_DATE.name to preferences[ConfigPreferencesKeys.CREATED_DATE],
            ConfigPreferencesKeys.CREATED_BY.name to preferences[ConfigPreferencesKeys.CREATED_BY],
            ConfigPreferencesKeys.UPDATED_AT.name to preferences[ConfigPreferencesKeys.UPDATED_AT],
            ConfigPreferencesKeys.UPDATED_BY.name to preferences[ConfigPreferencesKeys.UPDATED_BY],
        )
    }


    suspend fun saveAppPrefData(userData: Map<String, String>) {
        dataStore.edit { preferences ->
            userData.forEach { (key, value) ->
                Log.d("saveAppPrefData", " key:$key value $value")
                when (key) {
                    ConfigPreferencesKeys.ID.name -> preferences[ConfigPreferencesKeys.ID] =
                        value

                    ConfigPreferencesKeys.COMPANY_NAME.name -> preferences[ConfigPreferencesKeys.COMPANY_NAME] =
                        value

                    ConfigPreferencesKeys.LOGO.name -> preferences[ConfigPreferencesKeys.LOGO] =
                        value

                    ConfigPreferencesKeys.BASE_URL.name -> preferences[ConfigPreferencesKeys.BASE_URL] =
                        value

                    ConfigPreferencesKeys.VERSION.name -> preferences[ConfigPreferencesKeys.VERSION] =
                        value

                    ConfigPreferencesKeys.CREATED_DATE.name -> preferences[ConfigPreferencesKeys.CREATED_DATE] =
                        value

                    ConfigPreferencesKeys.CREATED_BY.name -> preferences[ConfigPreferencesKeys.CREATED_BY] =
                        value

                    ConfigPreferencesKeys.UPDATED_AT.name -> preferences[ConfigPreferencesKeys.UPDATED_AT] =
                        value

                    ConfigPreferencesKeys.UPDATED_BY.name -> preferences[ConfigPreferencesKeys.UPDATED_BY] =
                        value
                }
            }
        }
    }


    suspend fun getVersion(): String? {
        return dataStore.data
            .map { preferences ->
                preferences[ConfigPreferencesKeys.VERSION]
            }
            .first()
    }

    suspend fun getLogo(): String? {
        return dataStore.data
            .map { preferences ->
                preferences[ConfigPreferencesKeys.LOGO]
            }
            .first()
    }
}
