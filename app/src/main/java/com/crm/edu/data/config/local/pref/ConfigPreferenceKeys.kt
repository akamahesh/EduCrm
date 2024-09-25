package com.crm.edu.data.config.local.pref

import androidx.datastore.preferences.core.stringPreferencesKey

object ConfigPreferencesKeys {
    val ID = stringPreferencesKey("id")
    val COMPANY_NAME = stringPreferencesKey("company_name")
    val LOGO = stringPreferencesKey("logo")
    val BASE_URL = stringPreferencesKey("base_url")
    val VERSION = stringPreferencesKey("version")
    val CREATED_DATE = stringPreferencesKey("created_date")
    val CREATED_BY = stringPreferencesKey("created_by")
    val UPDATED_AT = stringPreferencesKey("updated_at")
    val UPDATED_BY = stringPreferencesKey("updated_by")
}
