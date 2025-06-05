package com.example.medimobile.data.remote

import com.example.medimobile.data.constants.ApiConstants
import com.example.medimobile.data.model.DropdownItem
import com.example.medimobile.data.model.PatientEncounter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


fun createRetrofit(isLowConnectivity: Boolean, dropdownMappings: Map<String, List<DropdownItem>>): Retrofit {
    val timeout = if (isLowConnectivity) ApiConstants.LOW_CONNECTIVITY_TIMEOUT else ApiConstants.REGULAR_TIMEOUT
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout, TimeUnit.SECONDS)
        .writeTimeout(timeout, TimeUnit.SECONDS)
        .build()

    val gson = GsonBuilder()
        .registerTypeAdapter(PatientEncounter::class.java, PatientEncounterDeserializer(dropdownMappings))
        .serializeNulls()
        .create()

    return Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
}

