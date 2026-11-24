package com.example.printshopmanager

import com.example.printshopmanager.model.Customer
import com.example.printshopmanager.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Fake in-memory Customer Repository for isolated unit testing
 */
class FakeCustomerRepository : CustomerRepository {
    private val customers = MutableStateFlow<List<Customer>>(emptyList())

    override fun getCustomersFlow(): Flow<List<Customer>> = customers

    override suspend fun getCustomerById(customerId: String): Result<Customer> {
        val item = customers.value.find { it.customerId == customerId }
        return if (item != null) Result.success(item)
        else Result.failure(Exception("Not found"))
    }

    override suspend fun addCustomer(customer: Customer): Result<String> {
        val id = if (customer.customerId.isNotBlank()) customer.customerId else "cust_${System.currentTimeMillis()}"
        val newCustomer = customer.copy(customerId = id)
        customers.value = customers.value + newCustomer
        return Result.success(id)
    }

    override suspend fun updateCustomer(customer: Customer): Result<Unit> {
        val current = customers.value.toMutableList()
        val index = current.indexOfFirst { it.customerId == customer.customerId }
        return if (index != -1) {
            current[index] = customer
            customers.value = current
            Result.success(Unit)
        } else {
            Result.failure(Exception("Not found"))
        }
    }

    override suspend fun deleteCustomer(customerId: String): Result<Unit> {
        customers.value = customers.value.filter { it.customerId != customerId }
        return Result.success(Unit)
    }

    override fun searchCustomers(query: String): Flow<List<Customer>> {
        val trimmed = query.trim().lowercase()
        return customers.map { list ->
            if (trimmed.isEmpty()) list
            else list.filter {
                it.fullName.lowercase().contains(trimmed) ||
                        it.phoneNumber.lowercase().contains(trimmed) ||
                        it.email.lowercase().contains(trimmed)
            }
        }
    }

    override suspend fun seedDummyData(): Result<Int> {
        val dummyList = (1..10).map { i ->
            Customer(
                customerId = "seed_$i",
                fullName = "Client $i",
                phoneNumber = "555-010$i",
                email = "client$i@example.com"
            )
        }
        customers.value = dummyList
        return Result.success(dummyList.size)
    }

    override fun getCustomerCountFlow(): Flow<Int> = customers.map { it.size }
}

class FakeCustomerRepositoryTest {

    private lateinit var repository: FakeCustomerRepository

    @Before
    fun setUp() {
        repository = FakeCustomerRepository()
    }

    @Test
    fun testAddAndRetrieveCustomer() = runBlocking {
        val customer = Customer(
            customerId = "c1",
            fullName = "Apex Studio",
            phoneNumber = "555-0101",
            email = "info@apex.example"
        )
        val addResult = repository.addCustomer(customer)
        assertTrue(addResult.isSuccess)

        val retrieved = repository.getCustomerById("c1").getOrThrow()
        assertEquals("Apex Studio", retrieved.fullName)
    }

    @Test
    fun testSearchCustomerByNameAndEmail() = runBlocking {
        repository.addCustomer(Customer(customerId = "1", fullName = "Metro University", email = "metro@univ.edu"))
        repository.addCustomer(Customer(customerId = "2", fullName = "Sweet Bites", email = "orders@sweetbites.com"))

        val searchByName = repository.searchCustomers("metro").first()
        assertEquals(1, searchByName.size)
        assertEquals("Metro University", searchByName[0].fullName)

        val searchByEmail = repository.searchCustomers("sweetbites").first()
        assertEquals(1, searchByEmail.size)
        assertEquals("Sweet Bites", searchByEmail[0].fullName)
    }

    @Test
    fun testDeleteCustomer() = runBlocking {
        repository.addCustomer(Customer(customerId = "del_1", fullName = "To Delete"))
        assertEquals(1, repository.getCustomerCountFlow().first())

        repository.deleteCustomer("del_1")
        assertEquals(0, repository.getCustomerCountFlow().first())
    }

    @Test
    fun testSeedDummyData() = runBlocking {
        val seedResult = repository.seedDummyData()
        assertTrue(seedResult.isSuccess)
        assertEquals(10, seedResult.getOrThrow())
        assertEquals(10, repository.getCustomerCountFlow().first())
    }
}
