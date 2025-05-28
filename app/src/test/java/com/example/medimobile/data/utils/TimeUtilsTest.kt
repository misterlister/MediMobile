package com.example.medimobile.data.utils

import com.example.medimobile.data.constants.DropdownConstants.NOT_SET
import com.example.medimobile.data.model.PatientEncounter
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

class TimeUtilsTest {

    private lateinit var originalTimeZone: TimeZone

    @Before
    fun setUp() {
        // Save original time zone and force PST for predictable results
        originalTimeZone = TimeZone.getDefault()
        TimeZone.setDefault(TimeZone.getTimeZone("America/Vancouver"))
    }

    @After
    fun tearDown() {
        // Restore original time zone
        TimeZone.setDefault(originalTimeZone)
    }

    // **Format arrival time tests**

    @Test
    fun `should return NOT_SET when both date and time are null`() {
        val encounter = PatientEncounter()
        val result = formatArrivalDateTime(encounter)
        assertEquals(NOT_SET, result)
    }

    @Test
    fun `should format correctly when only date is set`() {
        val encounter = PatientEncounter(
            arrivalDate = LocalDate.of(2025, 5, 27)
        )
        val result = formatArrivalDateTime(encounter)
        assertEquals("27/05/25 - $NOT_SET", result)
    }

    @Test
    fun `should format correctly when only time is set`() {
        val encounter = PatientEncounter(
            arrivalTime = LocalTime.of(14, 30)
        )
        val result = formatArrivalDateTime(encounter)
        assertEquals("$NOT_SET - 14:30", result)
    }

    @Test
    fun `should format correctly when both date and time are set`() {
        val encounter = PatientEncounter(
            arrivalDate = LocalDate.of(2025, 5, 27),
            arrivalTime = LocalTime.of(9, 15)
        )
        val result = formatArrivalDateTime(encounter)
        assertEquals("27/05/25 - 09:15", result)
    }

    // **Convert to UTC date string tests**

    @Test
    fun `should return null when date is null`() {
        val result = convertToUTCDateString(null)
        assertNull(result)
    }

    @Test
    fun `should convert local date to correct UTC ISO date string`() {
        val localDate = LocalDate.of(2025, 5, 27)
        val result = convertToUTCDateString(localDate)
        assertNotNull(result)

        // Parse result into a LocalDateTime
        val utcDateTime = LocalDateTime.parse(result, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        // Calculate expected UTC datetime
        val expectedUTC = localDate
            .atStartOfDay(ZoneId.of("America/Vancouver"))
            .withZoneSameInstant(ZoneOffset.UTC)
            .toLocalDateTime()

        assertEquals(expectedUTC, utcDateTime)
    }

    // **Convert to UTC date time string tests**

    @Test
    fun `should return null when date is null, time is not`() {
        val result = convertToUTCDateTimeString(null, LocalTime.of(12, 0))
        assertNull(result)
    }

    @Test
    fun `should return null when time is null, date is not`() {
        val result = convertToUTCDateTimeString(LocalDate.of(2025, 5, 27), null)
        assertNull(result)
    }

    @Test
    fun `should return null when both date and time are null`() {
        val result = convertToUTCDateTimeString(null, null)
        assertNull(result)
    }

    @Test
    fun `should convert date and time to correct UTC ISO string`() {
        val date = LocalDate.of(2025, 5, 27)
        val time = LocalTime.of(14, 30)
        val result = convertToUTCDateTimeString(date, time)
        assertNotNull(result)

        // Calculate expected UTC datetime
        val expectedUTC = ZonedDateTime.of(date, time, ZoneId.of("America/Vancouver"))
            .withZoneSameInstant(ZoneOffset.UTC)
            .toLocalDateTime()

        val actualUTC = LocalDateTime.parse(result, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        assertEquals(expectedUTC, actualUTC)
    }

    // **Get UTC year suffix tests**

    @Test
    fun `should return null instead of string when date is null`() {
        val result = getUTCYearSuffix(null)
        assertNull(result)
    }

    @Test
    fun `should return last two digits of year when date is valid`() {
        val result = getUTCYearSuffix(LocalDate.of(2025, 5, 27))
        assertEquals("25", result)
    }

    @Test
    fun `should return last two digits for year 1999`() {
        val result = getUTCYearSuffix(LocalDate.of(1999, 12, 31))
        assertEquals("99", result)
    }

    @Test
    fun `should return last two digits for year 2000`() {
        val result = getUTCYearSuffix(LocalDate.of(2000, 1, 1))
        assertEquals("00", result)
    }
}