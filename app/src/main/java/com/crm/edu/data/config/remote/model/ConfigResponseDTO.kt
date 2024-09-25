package com.crm.edu.data.login.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable




@Serializable
data class ConfigResponseDTO(
    @SerialName("status") var status: Int? = null,
    @SerialName("message") var message: String? = null,
    @SerialName("data") var configData: ConfigData? = null,

)

@Serializable
data class ConfigData(
    @SerialName("id") var id: String? = null,
    @SerialName("company_name") var companyName: String? = null,
    @SerialName("logo") var logo: String? = null,
    @SerialName("base_url") var baseUrl: String? = null,
    @SerialName("version") var version: String? = null,
    @SerialName("created_date") var createdDate: String? = null,
    @SerialName("created_by") var createdBy: String? = null,
    @SerialName("updated_at") var updatedAt: String? = null,
    @SerialName("updated_by") var updatedBy: String? = null
)
