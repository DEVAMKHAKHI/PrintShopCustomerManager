package com.example.printshopmanager

import com.example.printshopmanager.model.Customer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CustomerModelTest {

    @Test
    fun testInitialsGeneration_multipleWords() {
        val customer = Customer(fullName = "Apex Graphic Design Studio")
        assertEquals("AG", customer.initials)
    }

    @Test
    fun testInitialsGeneration_singleWord() {
        val customer = Customer(fullName = "PrintCare")
        assertEquals("PR", customer.initials)
    }

    @Test
    fun testInitialsGeneration_emptyString() {
        val customer = Customer(fullName = "")
        assertEquals("P", customer.initials)
    }

    @Test
    fun testCustomerToMapAndFromMap() {
        val original = Customer(
            customerId = "test_123",
            fullName = "Blue Horizon Architectural Group",
            phoneNumber = "555-0104",
            dateOfBirth = "1983-02-17",
            email = "plans@bluehorizon.example",
            address = "880 Skyline Tower, Chicago, IL",
            createdAt = 1700000000000L
        )

        val map = original.toMap()
        assertEquals("Blue Horizon Architectural Group", map["fullName"])
        assertEquals("555-0104", map["phoneNumber"])
        assertEquals("plans@bluehorizon.example", map["email"])

        val reconstructed = Customer.fromMap("test_123", map)
        assertEquals(original.customerId, reconstructed.customerId)
        assertEquals(original.fullName, reconstructed.fullName)
        assertEquals(original.phoneNumber, reconstructed.phoneNumber)
        assertEquals(original.email, reconstructed.email)
        assertEquals(original.address, reconstructed.address)
        assertEquals(original.createdAt, reconstructed.createdAt)
    }

    @Test
    fun testFormattedRegistrationDate_isNotBlank() {
        val customer = Customer(createdAt = System.currentTimeMillis())
        assertTrue(customer.formattedRegistrationDate.isNotBlank())
    }
}
