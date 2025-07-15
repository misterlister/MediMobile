package com.mgm.medimobile.data.model

data class LoggedInUser(
    val email: String,
    val group: String,
    val role: String,
    val accessToken: String,
    val tokenType: String,
    val uuid: String,
    val firstName: String?,
    val lastName: String?,
)
