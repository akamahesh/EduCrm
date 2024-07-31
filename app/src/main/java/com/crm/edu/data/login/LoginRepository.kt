package com.crm.edu.data.login

import android.util.Log
import com.crm.edu.core.EResult
import com.crm.edu.data.login.local.LocalDataSource
import com.crm.edu.data.login.local.pref.UserPreferencesKeys
import com.crm.edu.data.login.remote.api.LoginApi
import com.crm.edu.data.login.remote.model.LoginResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginApi: LoginApi,
    private val localDataSource: LocalDataSource
) {

    suspend fun login(username: String, password: String): Flow<EResult<LoginResponseDTO>> = flow {
        emit(EResult.Loading)
        try {
            val formData = HashMap<String, String>()
            formData["email"] = username
            formData["password"] = password
            val remoteData = loginApi.doLoginWithPassword(formData)
            //save user data
            val userMap = getUserMap(remoteData)
            Log.d("EduLogs", "user data : \n $userMap")
            localDataSource.saveUserData(userMap)
            localDataSource.setUserLoggedIn(true)
            emit(EResult.Success(remoteData))
        } catch (ex: Exception) {
            emit(EResult.Error(ex))
        }
    }.onStart {
        emit(EResult.Loading)
    }.catch { e ->
        emit(EResult.Error(e))
    }

    suspend fun isUserLoggedIn(): Boolean{
        return localDataSource.isUserLoggedIn()
    }

    private fun getUserMap(loginData: LoginResponseDTO): Map<String, String> {
        return mapOf(
            UserPreferencesKeys.STAFF_ID.name to loginData.userData?.staffid.toString(),
            UserPreferencesKeys.EMAIL.name to loginData.userData?.email.toString(),
            UserPreferencesKeys.FIRST_NAME.name to loginData.userData?.firstname.toString(),
            UserPreferencesKeys.LAST_NAME.name to loginData.userData?.lastname.toString(),
            UserPreferencesKeys.PHONE_NUMBER.name to loginData.userData?.phonenumber.toString(),
            UserPreferencesKeys.REPORTING_PERSON.name to loginData.userData?.reportingPerson.toString(),
            UserPreferencesKeys.PROFILE_IMAGE.name to loginData.userData?.profileImage.toString(),
            UserPreferencesKeys.LEAD_TYPE.name to loginData.userData?.leadType.toString(),
            UserPreferencesKeys.ACTIVE.name to loginData.userData?.active.toString(),
            UserPreferencesKeys.REPORTING_PERSON_NAME.name to loginData.userData?.reportingPerson.toString(),
            UserPreferencesKeys.STAFF_NAME.name to loginData.userData?.staffName.toString(),
            UserPreferencesKeys.DEPARTMENT_TYPE.name to loginData.userData?.departmentType.toString(),
            UserPreferencesKeys.OFFICE_LOCATION.name to loginData.userData?.officeLocation.toString(),
            UserPreferencesKeys.LATITUDE.name to loginData.userData?.latitude.toString(),
            UserPreferencesKeys.LONGITUDE.name to loginData.userData?.longitude.toString(),
            UserPreferencesKeys.DESIGNATION.name to loginData.userData?.designation.toString(),
            UserPreferencesKeys.CALLS_UPDATES.name to loginData.userData?.callsUpdates.toString(),
            UserPreferencesKeys.DEPARTMENT.name to loginData.userData?.department.toString(),
            // other thn user data
            UserPreferencesKeys.LOGIN_TOKEN.name to loginData.loginToken.toString(),
            UserPreferencesKeys.JWT_TOKEN.name to loginData.jwtToken.toString(),
        )

    }
}