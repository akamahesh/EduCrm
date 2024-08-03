package com.crm.edu.data.leaves.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val leavesApi: LeavesApi
) {

    suspend fun getLeavesData() = leavesApi.getHolidayData()
}