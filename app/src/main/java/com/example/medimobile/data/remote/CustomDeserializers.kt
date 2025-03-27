package com.example.medimobile.data.remote

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

// Deserializer for LocalDate
class LocalDateDeserializer : JsonDeserializer<LocalDate?> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate? {
        val dateTimeString = json?.asString
        return if (dateTimeString != null) {
            try {
                // Parse as LocalDateTime (since there's no timezone in the string)
                val localDateTime = LocalDateTime.parse(dateTimeString)

                // Convert to ZonedDateTime (with system default time zone) if needed
                val localDate = localDateTime.atZone(ZoneId.systemDefault()).toLocalDate()

                localDate
            } catch (e: Exception) {
                println("Error parsing date: $e")
                null
            }
        } else {
            null
        }
    }
}

// Deserializer for LocalTime
class LocalTimeDeserializer : JsonDeserializer<LocalTime?> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalTime? {
        val dateTimeString = json?.asString
        return if (dateTimeString != null) {
            try {
                // Parse as LocalDateTime
                val localDateTime = LocalDateTime.parse(dateTimeString)

                // Extract the time part from the LocalDateTime
                val localTime = localDateTime.toLocalTime()

                localTime
            } catch (e: Exception) {
                println("Error parsing time: $e")
                null
            }
        } else {
            null
        }
    }
}