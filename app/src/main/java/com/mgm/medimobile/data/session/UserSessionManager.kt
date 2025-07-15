package com.mgm.medimobile.data.session

import com.mgm.medimobile.data.model.LoggedInUser

object UserSessionManager {
    private var currentUser: LoggedInUser? = null

    val userEmail: String
        get() = currentUser?.email.orEmpty()

    val userUuid: String?
        get() = currentUser?.uuid

    val userGroup: String?
        get() = currentUser?.group

    val userRole: String?
        get() = currentUser?.role

    val userFirstName: String
        get() = currentUser?.firstName ?: ""

    val userLastName: String
        get() = currentUser?.lastName ?: ""

    val userFullName: String
        get() = "${currentUser?.firstName.orEmpty()} ${currentUser?.lastName.orEmpty()}".trim()

    val userDisplayName: String
        get() = when {
            userFullName.isNotBlank() -> userFullName
            userEmail.isNotBlank() -> userEmail
            else -> "Unknown User"
        }

    fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    fun login(user: LoggedInUser) {
        currentUser = user
    }

    fun logout() {
        currentUser = null
    }

    fun getAuthToken(): String {
        val user = currentUser
        return if (user != null && user.accessToken.isNotBlank()) {
            "${user.tokenType} ${user.accessToken}"
        } else {
            ""
        }
    }
}