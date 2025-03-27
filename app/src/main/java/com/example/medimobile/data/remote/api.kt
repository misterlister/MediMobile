package com.example.medimobile.data.remote

import com.example.medimobile.data.model.PatientEncounter
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

// Retrofit API Interface
interface AuthApi {
    @POST("api/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}

interface GetEncountersApi {
    @GET("api/medical/forms")
    suspend fun getPatientEncounters(
        @Query("user_uuid") userUuid: String? = null,
        @Query("arrival_date_min") arrivalDateMin: String? = null,
        @Query("arrival_date_max") arrivalDateMax: String? = null,
        @Header("Authorization") token: String
    ): Response<List<PatientEncounter>>
}