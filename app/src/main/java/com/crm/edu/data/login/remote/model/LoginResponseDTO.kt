package com.crm.edu.data.login.remote.model


import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("user_data") var userData: UserData? = UserData(),
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("login_token") var loginToken: String? = null,
    @SerializedName("followup_contact") var followupContact: ArrayList<FollowupContact> = arrayListOf(),
    @SerializedName("jwt_token") var jwtToken: String? = null,
)

data class UserData(

    @SerializedName("staffid") var staffid: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("firstname") var firstname: String? = null,
    @SerializedName("lastname") var lastname: String? = null,
    @SerializedName("facebook") var facebook: String? = null,
    @SerializedName("linkedin") var linkedin: String? = null,
    @SerializedName("phonenumber") var phonenumber: String? = null,
    @SerializedName("reporting_person") var reportingPerson: String? = null,
    @SerializedName("skype") var skype: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("datecreated") var datecreated: String? = null,
    @SerializedName("profile_image") var profileImage: String? = null,
    @SerializedName("last_ip") var lastIp: String? = null,
    @SerializedName("last_login") var lastLogin: String? = null,
    @SerializedName("last_activity") var lastActivity: String? = null,
    @SerializedName("last_password_change") var lastPasswordChange: String? = null,
    @SerializedName("new_pass_key") var newPassKey: String? = null,
    @SerializedName("new_pass_key_requested") var newPassKeyRequested: String? = null,
    @SerializedName("admin") var admin: String? = null,
    @SerializedName("role") var role: String? = null,
    @SerializedName("active") var active: String? = null,
    @SerializedName("default_language") var defaultLanguage: String? = null,
    @SerializedName("direction") var direction: String? = null,
    @SerializedName("media_path_slug") var mediaPathSlug: String? = null,
    @SerializedName("is_not_staff") var isNotStaff: String? = null,
    @SerializedName("hourly_rate") var hourlyRate: String? = null,
    @SerializedName("two_factor_auth_enabled") var twoFactorAuthEnabled: String? = null,
    @SerializedName("two_factor_auth_code") var twoFactorAuthCode: String? = null,
    @SerializedName("two_factor_auth_code_requested") var twoFactorAuthCodeRequested: String? = null,
    @SerializedName("email_signature") var emailSignature: String? = null,
    @SerializedName("birthday") var birthday: String? = null,
    @SerializedName("birthplace") var birthplace: String? = null,
    @SerializedName("sex") var sex: String? = null,
    @SerializedName("marital_status") var maritalStatus: String? = null,
    @SerializedName("nation") var nation: String? = null,
    @SerializedName("religion") var religion: String? = null,
    @SerializedName("identification") var identification: String? = null,
    @SerializedName("days_for_identity") var daysForIdentity: String? = null,
    @SerializedName("home_town") var homeTown: String? = null,
    @SerializedName("resident") var resident: String? = null,
    @SerializedName("current_address") var currentAddress: String? = null,
    @SerializedName("literacy") var literacy: String? = null,
    @SerializedName("orther_infor") var ortherInfor: String? = null,
    @SerializedName("job_position") var jobPosition: String? = null,
    @SerializedName("workplace") var workplace: String? = null,
    @SerializedName("place_of_issue") var placeOfIssue: String? = null,
    @SerializedName("account_number") var accountNumber: String? = null,
    @SerializedName("name_account") var nameAccount: String? = null,
    @SerializedName("issue_bank") var issueBank: String? = null,
    @SerializedName("records_received") var recordsReceived: String? = null,
    @SerializedName("Personal_tax_code") var PersonalTaxCode: String? = null,
    @SerializedName("team_manage") var teamManage: String? = null,
    @SerializedName("staff_identifi") var staffIdentifi: String? = null,
    @SerializedName("status_work") var statusWork: String? = null,
    @SerializedName("date_update") var dateUpdate: String? = null,
    @SerializedName("mail_password") var mailPassword: String? = null,
    @SerializedName("last_email_check") var lastEmailCheck: String? = null,
    @SerializedName("assign_state") var assignState: String? = null,
    @SerializedName("lead_type") var leadType: String? = null,
    @SerializedName("department_head") var departmentHead: String? = null,
    @SerializedName("facebook_lead_name") var facebookLeadName: String? = null,

    )


data class FollowupContact(
    @SerializedName("name") var name: String? = null,
    @SerializedName("phonenumber") var phonenumber: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("date") var date: String? = null,
)