package com.example.medimobile.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

// Retrofit API Interface
interface AuthApi {
    @POST("login")
    suspend fun login(@Body credentials: LoginRequest): LoginResponse
}