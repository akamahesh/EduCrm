package com.crm.edu.data.leaverequest.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LeaveRequestDetailDTO(
    @SerialName("leave_type") var leaveType: ArrayList<LeaveType> = arrayListOf()
)

@Serializable
data class LeaveType(
    @SerialName("id") var id: String? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("status") var status: String? = null,
    @SerialName("created_by") var createdBy: String? = null,
    @SerialName("created_date") var createdDate: String? = null,
    @SerialName("updated_by") var updatedBy: String? = null,
    @SerialName("updated_date") var updatedDate: String? = null,
    @SerialName("months") var months: String? = null,
    @SerialName("leaves") var leaves: String? = null,
    @SerialName("apply_date") var applyDate: String? = null
)


@Serializable
data class LeaveRequestResponseDTO(
    @SerialName("status") var status: Int,
    @SerialName("message") var message: String? = null,
)