package com.example.medimobile.viewmodel

import com.example.medimobile.testdata.TestData.mockEvents
import com.example.medimobile.data.eventdata.EventList
import com.example.medimobile.data.model.DropdownItem
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.model.StageStatus
import com.example.medimobile.data.utils.DateRangeOption
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class MediMobileViewModelTest {
    private lateinit var viewModel: MediMobileViewModel

    companion object {
        @JvmStatic
        @BeforeClass
        fun setupClass() {
            EventList.EVENTS = mockEvents // override the events list before the testing
        }
    }

    @Before
    fun setup() {
        // reset the viewmodel before each test
        viewModel = MediMobileViewModel()
    }

    // **Initialization tests**

    @Test
    fun `initial selected event and location are set to first of mock list`() = runTest {
        val selectedEvent = viewModel.selectedEvent.first()
        val selectedLocation = viewModel.selectedLocation.first()

        assertEquals(mockEvents.first(), selectedEvent)
        assertEquals(mockEvents.first().locations.first(), selectedLocation)
    }

    @Test
    fun `selected date range initializes to WEEK and updates correctly`() = runTest {
        // Confirm initial default of week
        val initial = viewModel.selectedDateRange.first()
        assertEquals(DateRangeOption.WEEK, initial)

        // Update and verify change to month
        viewModel.setSelectedDateRange(DateRangeOption.MONTH)
        val updated = viewModel.selectedDateRange.first()
        assertEquals(DateRangeOption.MONTH, updated)
    }

    @Test
    fun `low connectivity mode initializes to false and updates correctly`() = runTest {
        // Check initial value is false
        val initial = viewModel.lowConnectivityMode.value
        assertFalse(initial)

        // Update the value to true
        viewModel.setLowConnectivityMode(true)
        val updated = viewModel.lowConnectivityMode.value
        assertTrue(updated)
    }

    @Test
    fun `setSelectedEvent updates the selected event`() = runTest {
        val newEvent = mockEvents[1]
        viewModel.setSelectedEvent(newEvent.eventName)

        val selectedEvent = viewModel.selectedEvent.first()
        assertEquals(newEvent, selectedEvent)
    }

    @Test
    fun `setSelectedEvent sets selectedEvent to null when name does not match`() = runTest {
        viewModel.setSelectedEvent("Not a real event")

        assertNull(viewModel.selectedEvent.value)
    }

    @Test
    fun `selected event dropdowns are correct`() = runTest {
        viewModel.setSelectedEvent(mockEvents[0].eventName)
        val selectedEvent = viewModel.selectedEvent.first()

        assertEquals(2, selectedEvent?.dropdowns?.arrivalMethods?.size)
        assertEquals("Method 1", selectedEvent?.dropdowns?.arrivalMethods?.first()?.displayValue)
    }

    @Test
    fun `setSelectedLocation updates the selected location`() = runTest {
        val newLocation = mockEvents[1].locations[1]

        viewModel.setSelectedLocation(newLocation)
        val selectedLocation = viewModel.selectedLocation.first()

        assertEquals(newLocation, selectedLocation)
    }

    @Test
    fun `setCurrentEncounter copies encounter and resets dataChanged`() = runTest {
        val encounter = PatientEncounter(event = "Event1", location = "Loc1", userUuid = "user1", age = 30)
        viewModel.markDataChanged()  // simulate that data had changed

        viewModel.setCurrentEncounter(encounter)

        val current = viewModel.currentEncounter.value
        assertNotSame(encounter, current)  // ensure it's a copy, not the same object
        assertEquals(encounter.age, current?.age)
        assertFalse(viewModel.dataChanged.value)  // make sure dataChanged is reset to false
    }

    // **Stage status tests**

    // Arrival stage tests

    @Test
    fun `updateArrivalStatus sets NOT_STARTED when no fields filled`() = runTest {
        val encounter = PatientEncounter()
        viewModel.setCurrentEncounter(encounter)

        viewModel.updateArrivalStatus()

        assertEquals(StageStatus.NOT_STARTED, viewModel.currentEncounter.value?.arrivalStatus)
    }

    @Test
    fun `updateArrivalStatus sets IN_PROGRESS when some fields filled`() = runTest {
        val encounter = PatientEncounter(
            arrivalDate = LocalDate.now(),
            arrivalTime = LocalTime.now()
        )
        viewModel.setCurrentEncounter(encounter)

        viewModel.updateArrivalStatus()

        assertEquals(StageStatus.IN_PROGRESS, viewModel.currentEncounter.value?.arrivalStatus)
    }

    @Test
    fun `updateArrivalStatus sets COMPLETE when all fields filled`() = runTest {
        val encounter = PatientEncounter(
            arrivalDate = LocalDate.now(),
            arrivalTime = LocalTime.now(),
            triageAcuity = "Red",
            visitId = "V123",
            arrivalMethod = "Method1"
        )
        viewModel.setCurrentEncounter(encounter)

        viewModel.updateArrivalStatus()

        assertEquals(StageStatus.COMPLETE, viewModel.currentEncounter.value?.arrivalStatus)
    }

    // Triage stage tests

    @Test
    fun `updateTriageStatus sets NOT_STARTED when no fields filled`() = runTest {
        val encounter = PatientEncounter()
        viewModel.setCurrentEncounter(encounter)

        viewModel.updateTriageStatus()

        assertEquals(StageStatus.NOT_STARTED, viewModel.currentEncounter.value?.triageStatus)
    }

    @Test
    fun `updateTriageStatus sets IN_PROGRESS when some fields filled`() = runTest {
        val encounter = PatientEncounter(
            age = 40
        )
        viewModel.setCurrentEncounter(encounter)

        viewModel.updateTriageStatus()

        assertEquals(StageStatus.IN_PROGRESS, viewModel.currentEncounter.value?.triageStatus)
    }

    @Test
    fun `updateTriageStatus sets COMPLETE when all fields filled`() = runTest {
        val encounter = PatientEncounter(
            age = 30,
            role = "Artist",
            chiefComplaint = "Headache"
        )
        viewModel.setCurrentEncounter(encounter)

        viewModel.updateTriageStatus()

        assertEquals(StageStatus.COMPLETE, viewModel.currentEncounter.value?.triageStatus)
    }

    // Discharge stage tests

    @Test
    fun `updateDischargeStatus sets NOT_STARTED when no fields filled`() = runTest {
        val encounter = PatientEncounter()
        viewModel.setCurrentEncounter(encounter)

        viewModel.updateDischargeStatus()

        assertEquals(StageStatus.NOT_STARTED, viewModel.currentEncounter.value?.dischargeStatus)
    }

    @Test
    fun `updateDischargeStatus sets IN_PROGRESS when some fields filled`() = runTest {
        val encounter = PatientEncounter(
            departureDate = LocalDate.now(),
            departureTime = LocalTime.now()
        )
        viewModel.setCurrentEncounter(encounter)

        viewModel.updateDischargeStatus()

        assertEquals(StageStatus.IN_PROGRESS, viewModel.currentEncounter.value?.dischargeStatus)
    }

    @Test
    fun `updateDischargeStatus sets COMPLETE when all fields filled`() = runTest {
        val encounter = PatientEncounter(
            departureDate = LocalDate.now(),
            departureTime = LocalTime.now(),
            departureDest = "Home"
        )
        viewModel.setCurrentEncounter(encounter)

        viewModel.updateDischargeStatus()

        assertEquals(StageStatus.COMPLETE, viewModel.currentEncounter.value?.dischargeStatus)
    }

    // **Setter tests**

    @Test
    fun `setAge updates encounter and marks dataChanged when value changes`() = runTest {
        val original = PatientEncounter(age = 25)
        viewModel.setCurrentEncounter(original)

        // Verify initial state
        val beforeUpdate = viewModel.currentEncounter.value
        assertEquals(25, beforeUpdate?.age)
        assertFalse(viewModel.dataChanged.value)

        viewModel.setAge(30)

        // Verify updated state
        val updated = viewModel.currentEncounter.value
        assertEquals(30, updated?.age)
        assertTrue(viewModel.dataChanged.value)
    }

    @Test
    fun `setAge does not update encounter or mark dataChanged when value is the same`() = runTest {
        val original = PatientEncounter(age = 25)
        viewModel.setCurrentEncounter(original)
        viewModel.setAge(25)

        val updated = viewModel.currentEncounter.value
        assertEquals(25, updated?.age)
        assertFalse(viewModel.dataChanged.value)
    }

    @Test
    fun `setChiefComplaint updates encounter and marks dataChanged when value changes`() = runTest {
        val original = PatientEncounter(chiefComplaint = "Headache")
        viewModel.setCurrentEncounter(original)

        val beforeUpdate = viewModel.currentEncounter.value
        assertEquals("Headache", beforeUpdate?.chiefComplaint)
        assertFalse(viewModel.dataChanged.value)

        viewModel.setChiefComplaint("Fever")

        val updated = viewModel.currentEncounter.value
        assertEquals("Fever", updated?.chiefComplaint)
        assertTrue(viewModel.dataChanged.value)
    }

    @Test
    fun `setChiefComplaint does not update encounter or mark dataChanged when value is the same`() = runTest {
        val original = PatientEncounter(chiefComplaint = "Headache")
        viewModel.setCurrentEncounter(original)

        viewModel.setChiefComplaint("Headache")

        val updated = viewModel.currentEncounter.value
        assertEquals("Headache", updated?.chiefComplaint)
        assertFalse(viewModel.dataChanged.value)
    }

    @Test
    fun `setArrivalTime updates encounter and marks dataChanged when value changes`() = runTest {
        val original = PatientEncounter(arrivalTime = LocalTime.of(10, 0))
        viewModel.setCurrentEncounter(original)

        val beforeUpdate = viewModel.currentEncounter.value
        assertEquals(LocalTime.of(10, 0), beforeUpdate?.arrivalTime)
        assertFalse(viewModel.dataChanged.value)

        viewModel.setArrivalTime(LocalTime.of(11, 0))

        val updated = viewModel.currentEncounter.value
        assertEquals(LocalTime.of(11, 0), updated?.arrivalTime)
        assertTrue(viewModel.dataChanged.value)
    }

    @Test
    fun `setArrivalTime does not update encounter or mark dataChanged when value is the same`() = runTest {
        val original = PatientEncounter(arrivalTime = LocalTime.of(10, 0))
        viewModel.setCurrentEncounter(original)

        viewModel.setArrivalTime(LocalTime.of(10, 0))

        val updated = viewModel.currentEncounter.value
        assertEquals(LocalTime.of(10, 0), updated?.arrivalTime)
        assertFalse(viewModel.dataChanged.value)
    }

    @Test
    fun `setArrivalDate updates encounter and marks dataChanged when value changes`() = runTest {
        val original = PatientEncounter(arrivalDate = LocalDate.of(2023, 1, 1))
        viewModel.setCurrentEncounter(original)

        viewModel.setArrivalDate(LocalDate.of(2023, 1, 2))

        val updated = viewModel.currentEncounter.value
        assertEquals(LocalDate.of(2023, 1, 2), updated?.arrivalDate)
        assertTrue(viewModel.dataChanged.value)
    }

    @Test
    fun `setArrivalDate does not update encounter or mark dataChanged when value is the same`() = runTest {
        val date = LocalDate.of(2023, 1, 1)
        val original = PatientEncounter(arrivalDate = date)
        viewModel.setCurrentEncounter(original)

        viewModel.setArrivalDate(date)

        assertEquals(date, viewModel.currentEncounter.value?.arrivalDate)
        assertFalse(viewModel.dataChanged.value)
    }


    @Test
    fun `setArrivalMethod updates encounter and marks dataChanged when value changes`() = runTest {
        val original = PatientEncounter(arrivalMethod = "Walk-in")
        viewModel.setCurrentEncounter(original)

        viewModel.setArrivalMethod("Ambulance")

        val updated = viewModel.currentEncounter.value
        assertEquals("Ambulance", updated?.arrivalMethod)
        assertTrue(viewModel.dataChanged.value)
    }

    @Test
    fun `setArrivalMethod does not update encounter or mark dataChanged when value is the same`() = runTest {
        val original = PatientEncounter(arrivalMethod = "Walk-in")
        viewModel.setCurrentEncounter(original)

        viewModel.setArrivalMethod("Walk-in")

        assertEquals("Walk-in", viewModel.currentEncounter.value?.arrivalMethod)
        assertFalse(viewModel.dataChanged.value)
    }

    @Test
    fun `setComment updates encounter and marks dataChanged when value changes`() = runTest {
        val original = PatientEncounter(comment = "Initial note")
        viewModel.setCurrentEncounter(original)

        viewModel.setComment("Updated note")

        val updated = viewModel.currentEncounter.value
        assertEquals("Updated note", updated?.comment)
        assertTrue(viewModel.dataChanged.value)
    }

    @Test
    fun `setComment does not update encounter or mark dataChanged when value is the same`() = runTest {
        val original = PatientEncounter(comment = "Initial note")
        viewModel.setCurrentEncounter(original)

        viewModel.setComment("Initial note")

        assertEquals("Initial note", viewModel.currentEncounter.value?.comment)
        assertFalse(viewModel.dataChanged.value)
    }

    @Test
    fun `setDepartureDate updates encounter and marks dataChanged when value changes`() = runTest {
        val original = PatientEncounter(departureDate = LocalDate.of(2023, 1, 1))
        viewModel.setCurrentEncounter(original)

        viewModel.setDepartureDate(LocalDate.of(2023, 1, 2))

        val updated = viewModel.currentEncounter.value
        assertEquals(LocalDate.of(2023, 1, 2), updated?.departureDate)
        assertTrue(viewModel.dataChanged.value)
    }

    @Test
    fun `setDepartureDate does not update encounter or mark dataChanged when value is the same`() = runTest {
        val date = LocalDate.of(2023, 1, 1)
        val original = PatientEncounter(departureDate = date)
        viewModel.setCurrentEncounter(original)

        viewModel.setDepartureDate(date)

        assertEquals(date, viewModel.currentEncounter.value?.departureDate)
        assertFalse(viewModel.dataChanged.value)
    }

    @Test
    fun `setDepartureTime updates encounter and marks dataChanged when value changes`() = runTest {
        val original = PatientEncounter(departureTime = LocalTime.of(10, 0))
        viewModel.setCurrentEncounter(original)

        viewModel.setDepartureTime(LocalTime.of(11, 0))

        val updated = viewModel.currentEncounter.value
        assertEquals(LocalTime.of(11, 0), updated?.departureTime)
        assertTrue(viewModel.dataChanged.value)
    }

    @Test
    fun `setDepartureTime does not update encounter or mark dataChanged when value is the same`() = runTest {
        val time = LocalTime.of(10, 0)
        val original = PatientEncounter(departureTime = time)
        viewModel.setCurrentEncounter(original)

        viewModel.setDepartureTime(time)

        assertEquals(time, viewModel.currentEncounter.value?.departureTime)
        assertFalse(viewModel.dataChanged.value)
    }

    @Test
    fun `setDepartureDest updates encounter and marks dataChanged when value changes`() = runTest {
        val original = PatientEncounter(departureDest = "Home")
        viewModel.setCurrentEncounter(original)

        viewModel.setDepartureDest("Hospital")

        val updated = viewModel.currentEncounter.value
        assertEquals("Hospital", updated?.departureDest)
        assertTrue(viewModel.dataChanged.value)
    }

    @Test
    fun `setDepartureDest does not update encounter or mark dataChanged when value is the same`() = runTest {
        val original = PatientEncounter(departureDest = "Home")
        viewModel.setCurrentEncounter(original)

        viewModel.setDepartureDest("Home")

        assertEquals("Home", viewModel.currentEncounter.value?.departureDest)
        assertFalse(viewModel.dataChanged.value)
    }

    @Test
    fun `setRole updates encounter and marks dataChanged when value changes`() = runTest {
        val original = PatientEncounter(role = "Artist")
        viewModel.setCurrentEncounter(original)

        viewModel.setRole("Vendor")

        val updated = viewModel.currentEncounter.value
        assertEquals("Vendor", updated?.role)
        assertTrue(viewModel.dataChanged.value)
    }

    @Test
    fun `setRole does not update encounter or mark dataChanged when value is the same`() = runTest {
        val original = PatientEncounter(role = "Artist")
        viewModel.setCurrentEncounter(original)

        viewModel.setRole("Artist")

        assertEquals("Artist", viewModel.currentEncounter.value?.role)
        assertFalse(viewModel.dataChanged.value)
    }

    @Test
    fun `setVisitId updates encounter and marks dataChanged when value changes`() = runTest {
        val original = PatientEncounter(visitId = "12345")
        viewModel.setCurrentEncounter(original)

        viewModel.setVisitId("67890")

        val updated = viewModel.currentEncounter.value
        assertEquals("67890", updated?.visitId)
        assertTrue(viewModel.dataChanged.value)
    }

    @Test
    fun `setVisitId does not update encounter or mark dataChanged when value is the same`() = runTest {
        val original = PatientEncounter(visitId = "12345")
        viewModel.setCurrentEncounter(original)

        viewModel.setVisitId("12345")

        assertEquals("12345", viewModel.currentEncounter.value?.visitId)
        assertFalse(viewModel.dataChanged.value)
    }

    @Test
    fun `setTriageAcuity updates encounter and marks dataChanged when value changes`() = runTest {
        val original = PatientEncounter(triageAcuity = "Red")
        viewModel.setCurrentEncounter(original)

        viewModel.setTriageAcuity("Green")

        val updated = viewModel.currentEncounter.value
        assertEquals("Green", updated?.triageAcuity)
        assertTrue(viewModel.dataChanged.value)
    }

    @Test
    fun `setTriageAcuity does not update encounter or mark dataChanged when value is the same`() = runTest {
        val original = PatientEncounter(triageAcuity = "Red")
        viewModel.setCurrentEncounter(original)

        viewModel.setTriageAcuity("Red")

        assertEquals("Red", viewModel.currentEncounter.value?.triageAcuity)
        assertFalse(viewModel.dataChanged.value)
    }

    @Test
    fun `setDischargeDiagnosis updates encounter and marks dataChanged when value changes`() = runTest {
        val original = PatientEncounter(dischargeDiagnosis = "Flu")
        viewModel.setCurrentEncounter(original)

        viewModel.setDischargeDiagnosis("Cold")

        val updated = viewModel.currentEncounter.value
        assertEquals("Cold", updated?.dischargeDiagnosis)
        assertTrue(viewModel.dataChanged.value)
    }

    @Test
    fun `setDischargeDiagnosis does not update encounter or mark dataChanged when value is the same`() = runTest {
        val original = PatientEncounter(dischargeDiagnosis = "Flu")
        viewModel.setCurrentEncounter(original)

        viewModel.setDischargeDiagnosis("Flu")

        assertEquals("Flu", viewModel.currentEncounter.value?.dischargeDiagnosis)
        assertFalse(viewModel.dataChanged.value)
    }

    // Dropdown mapping tests

    @Test
    fun `getDropdownMappings returns empty map when no event is selected`() = runTest {
        viewModel.setSelectedEvent("Not a real event")
        assertNull(viewModel.selectedEvent.value)

        val result = viewModel.getDropdownMappings()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getDropdownMappings returns correct values for Event 2`() = runTest {
        viewModel.setSelectedEvent("Event 2")
        val mappings = viewModel.getDropdownMappings()

        assertEquals(
            listOf(
                DropdownItem("method3", "Method 3"),
                DropdownItem("method4", "Method 4")),
            mappings["arrival_method"]
        )
        assertEquals(
            listOf(
                DropdownItem("dest3", "Destination 3"),
                DropdownItem("dest4", "Destination 4")),
            mappings["departure_dest"]
        )
        assertEquals(
            listOf(
                DropdownItem("role3", "Role 3"),
                DropdownItem("role4", "Role 4")),
            mappings["role"]
        )
        assertEquals(
            listOf(
                DropdownItem("complaint3", "Complaint 3"),
                DropdownItem("complaint4", "Complaint 4")),
            mappings["chief_complaint"]
        )
    }
}