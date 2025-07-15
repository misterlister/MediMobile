package com.mgm.medimobile.data.remote

import com.google.gson.annotations.SerializedName

// Data Classes for API
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("user_group") val userGroup: String
)

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("user_role") val userRole: String,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?
)

data class ErrorResponse(
    @SerializedName("detail") val detail: String
)

data class SequenceResponse(
    @SerializedName("next_number") val nextNumber: Int
)