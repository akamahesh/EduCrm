package com.crm.edu.utils

class Constants {

    companion object {
        const val URL_PROTOCOL_HTTPS = "https://"
    }

    object Endpoints {

        const val PROD_ENDPOINT = "https://crm.educationvibes.in/"
        const val STAGING_ENDPOINT = "https://crm.staging.educationvibes.in/external/"

    }

    object ErrorMessage {
        const val UNKNOWN_ERROR = "An unknown error occurred!"
        const val NO_SUCH_DATA = "Data not found in the database"
        const val USER_NOT_LOGGED_IN = "User is not Logged-in"
    }
}