package com.crm.edu.data.markAttendance.remote

import kotlinx.serialization.Serializable

@Serializable
data class MarkAttendanceResponseDTO(
    val status: Int,
    val message: String
)

fun MarkAttendanceResponseDTO.asData() = MarkAttendanceData(
    status = status,
    message = message
)