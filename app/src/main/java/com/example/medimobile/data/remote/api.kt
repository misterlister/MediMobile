package com.example.medimobile.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Retrofit API Interface
interface AuthApi {
    @POST("api/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}