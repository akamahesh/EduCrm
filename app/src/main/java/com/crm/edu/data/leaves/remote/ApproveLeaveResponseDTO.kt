package com.crm.edu.data.leaves.remote

import com.crm.edu.data.leaves.ApproveLeaveData
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ApproveLeaveResponseDTO(
    @SerialName("status") var status: Int,
    @SerialName("message") var message: String,
)

fun ApproveLeaveResponseDTO.asData() = ApproveLeaveData(
    status = status,
    message = message
)