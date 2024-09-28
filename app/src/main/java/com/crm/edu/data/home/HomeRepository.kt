package com.crm.edu.data.home

import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val localDataSource: HomeLocalDataSource
) {

    suspend fun getHomeConfiguration(): HomeConfigurationData {
        return HomeConfigurationData(localDataSource.getCallStatus() == "1")
    }

}

data class HomeConfigurationData(
    val showCallManager: Boolean
)