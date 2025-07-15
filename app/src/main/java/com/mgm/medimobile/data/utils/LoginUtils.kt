package com.mgm.medimobile.data.utils

import com.mgm.medimobile.data.model.LoggedInUser
import com.mgm.medimobile.data.remote.LoginResponse

fun LoginResponse.toLoggedInUser(
    email: String,
    group: String
): LoggedInUser {
    return LoggedInUser(
        email = email,
        group = group,
        role = this.userRole,
        accessToken = this.accessToken,
        tokenType = this.tokenType,
        uuid = this.uuid,
        firstName = this.firstName,
        lastName = this.lastName
    )
}