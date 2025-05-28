package com.example.medimobile.data.utils

import com.example.medimobile.data.constants.IDConstants
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class StringUtilsTest {

    // **Date range conversion tests**

    @Test
    fun `toDisplayValue returns correct string for each DateRangeOption`() {
        assertEquals("Day", DateRangeOption.DAY.toDisplayValue())
        assertEquals("Week", DateRangeOption.WEEK.toDisplayValue())
        assertEquals("Month", DateRangeOption.MONTH.toDisplayValue())
        assertEquals("Year", DateRangeOption.YEAR.toDisplayValue())
        assertEquals("All Time", DateRangeOption.ALL_TIME.toDisplayValue())
    }

    @Test
    fun `toDateRangeOption returns correct enum for valid strings`() {
        assertEquals(DateRangeOption.DAY, "day".toDateRangeOption())
        assertEquals(DateRangeOption.WEEK, "Week".toDateRangeOption())
        assertEquals(DateRangeOption.MONTH, "MONTH".toDateRangeOption())
        assertEquals(DateRangeOption.YEAR, "yEaR".toDateRangeOption())
        assertEquals(DateRangeOption.ALL_TIME, "All Time".toDateRangeOption())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `toDateRangeOption throws exception for unknown string`() {
        "invalid".toDateRangeOption()
    }

    // **Format Visit ID tests**

    @Test
    fun `formatVisitID returns correct string for QR_CODE category`() {
        val date = LocalDate.of(2025, 5, 27)
        val result = formatVisitID(
            IDConstants.VisitIDCategory.QR_CODE,
            eventID = "SHA",
            locationID = "M01",
            date = date,
            visitSuffix = 7
        )

        assertEquals(result, "QSHAM0125-00007")
    }

    @Test
    fun `formatVisitID returns correct string for GENERATED category`() {
        val date = LocalDate.of(2025, 5, 27)
        val result = formatVisitID(
            IDConstants.VisitIDCategory.GENERATED,
            eventID = "RAN",
            locationID = "DOM",
            date = date,
            visitSuffix = 654
        )

        assertEquals(result, "GRANDOM25-00654")
    }

    @Test
    fun `formatVisitID returns correct string for unusual values`() {
        val date = LocalDate.of(1000, 5, 27)
        val result = formatVisitID(
            IDConstants.VisitIDCategory.QR_CODE,
            eventID = "A",
            locationID = "LONGLOCATION",
            date = date,
            visitSuffix = 123456789
        )

        assertEquals(result, "QALONGLOCATION00-123456789")
    }
}