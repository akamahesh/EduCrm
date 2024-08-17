package com.crm.edu.data.login.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDTO(
    @SerialName("user_data") var userData: UserData? = null,
    @SerialName("status") var status: Int? = null,
    @SerialName("message") var message: String? = null,
    @SerialName("login_token") var loginToken: String? = null,
    @SerialName("followup_contact") var followupContact: ArrayList<String> = arrayListOf(),
    @SerialName("jwt_token") var jwtToken: String? = null

)

@Serializable
data class UserData(
    @SerialName("staffid") var staffid: String? = null,
    @SerialName("email") var email: String? = null,
    @SerialName("firstname") var firstname: String? = null,
    @SerialName("lastname") var lastname: String? = null,
    @SerialName("phonenumber") var phonenumber: String? = null,
    @SerialName("reporting_person") var reportingPerson: String? = null,
    @SerialName("profile_image") var profileImage: String? = null,
    @SerialName("lead_type") var leadType: String? = null,
    @SerialName("active") var active: String? = null,
    @SerialName("reporting_person_name") var reportingPersonName: String? = null,
    @SerialName("staff_name") var staffName: String? = null,
    @SerialName("department_type") var departmentType: String? = null,
    @SerialName("office_location") var officeLocation: String? = null,
    @SerialName("latitude") var latitude: String? = null,
    @SerialName("longitude") var longitude: String? = null,
    @SerialName("designation") var designation: String? = null,
    @SerialName("calls_updates") var callsUpdates: String? = null,
    @SerialName("department_id") var departmentId: String? = null

)

@Serializable
data class FollowupContact(
    @SerialName("name") var name: String? = null,
    @SerialName("phonenumber") var phonenumber: String? = null,
    @SerialName("email") var email: String? = null,
    @SerialName("date") var date: String? = null,
)