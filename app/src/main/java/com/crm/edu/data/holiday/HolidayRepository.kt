package com.crm.edu.data.holiday

import com.crm.edu.core.EResult
import com.crm.edu.data.holiday.local.HolidayEntity
import com.crm.edu.data.holiday.local.LocalDataSource
import com.crm.edu.data.holiday.local.asExternalModel
import com.crm.edu.data.holiday.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class HolidayRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    fun getHolidayList(): Flow<EResult<List<HolidayData>>> = flow {
        emit(EResult.Loading)
        val localData = localDataSource.getHolidayEntities().firstOrNull() ?: emptyList()
        emit(EResult.SuccessAndLoading(localData.map(HolidayEntity::asExternalModel)))
        try {
            val remoteData = remoteDataSource.getHolidayData()
            val holidayEntities = remoteData.holidayListDTO.map {
                it.asEntity()
            }
            localDataSource.insertHolidayEntities(holidayEntities)
            emit(EResult.Success(holidayEntities.map(HolidayEntity::asExternalModel)))
        } catch (ex: Exception) {
            emit(EResult.Error(ex))
        }
    }.onStart {
        emit(EResult.Loading)
    }.catch { e ->
        emit(EResult.Error(e))
    }

}