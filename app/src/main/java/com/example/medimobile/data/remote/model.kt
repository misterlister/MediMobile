package com.example.medimobile.data.remote

// Data Classes for API
data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String)