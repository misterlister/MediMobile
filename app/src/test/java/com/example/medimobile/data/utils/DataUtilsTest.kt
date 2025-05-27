package com.example.medimobile.data.utils

import com.example.medimobile.data.constants.DropdownConstants.NOT_SET
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DataUtilsTest {

    @Test
    fun `string cases`() {
        assertTrue(isDataEmptyOrNull(null))
        assertTrue(isDataEmptyOrNull(""))
        assertTrue(isDataEmptyOrNull(NOT_SET))
        assertFalse(isDataEmptyOrNull("hello"))
        assertFalse(isDataEmptyOrNull(" "))
    }

    @Test
    fun `int cases`() {
        assertTrue(isDataEmptyOrNull(0))
        assertFalse(isDataEmptyOrNull(5))
        assertFalse(isDataEmptyOrNull(-3))
    }

    @Test
    fun `other types`() {
        assertTrue(isDataEmptyOrNull(null as Any?))
        assertFalse(isDataEmptyOrNull(Any()))
        assertFalse(isDataEmptyOrNull(listOf<Int>()))
        assertFalse(isDataEmptyOrNull(listOf(1, 2)))
        assertFalse(isDataEmptyOrNull(0L))
    }
}