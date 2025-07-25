package com.mgm.medimobile.data.remote

import com.mgm.medimobile.data.model.PatientEncounter
import com.mgm.medimobile.data.model.PatientEncounterFormData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

// Retrofit API Interface
interface AuthApi {
    @POST("api/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}

interface GetEncountersApi {
    // Get list of patient encounters
    @GET("api/medical/forms")
    suspend fun getPatientEncounters(
        @Query("user_uuid") userUuid: String? = null,
        @Query("arrival_datetime_min") arrivalDateTimeMin: String? = null,
        @Query("arrival_datetime_max") arrivalDateTimeMax: String? = null,
        @Header("Authorization") token: String
    ): Response<List<PatientEncounter>>
}

interface SubmitEncountersApi {
    // Submit a new patient encounter
    @POST("api/medical/form")
    suspend fun postPatientEncounter(
        @Body patientEncounter: PatientEncounterFormData,
        @Header("Authorization") token: String
    ): Response<PatientEncounter>

    // Update a patient encounter
    @PUT("api/medical/form")
    suspend fun updatePatientEncounter(
        @Body patientEncounter: PatientEncounterFormData,
        @Header("Authorization") token: String
    ): Response<PatientEncounter>
}

interface GetSequenceApi {
    // Get the next visit ID sequence number
    @GET("api/medical/form/next-visit-id")
    suspend fun getNextVisitIDSequence(): Response<SequenceResponse>
}

interface GetEncounterByVisitIdApi {
    // Get a patient encounter by visit ID
    @GET("api/medical/form_by_visit_id")
    suspend fun getEncounterByVisitId(
        @Query("visit_id") visitId: String,
        @Header("Authorization") token: String
    ): Response<PatientEncounter>
}