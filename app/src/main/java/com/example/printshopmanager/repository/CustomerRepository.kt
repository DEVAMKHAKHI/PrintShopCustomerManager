package com.example.printshopmanager.repository

import com.example.printshopmanager.firebase.DummyDataSeeder
import com.example.printshopmanager.firebase.FirebaseManager
import com.example.printshopmanager.model.Customer
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

interface CustomerRepository {
    fun getCustomersFlow(): Flow<List<Customer>>
    suspend fun getCustomerById(customerId: String): Result<Customer>
    suspend fun addCustomer(customer: Customer): Result<String>
    suspend fun updateCustomer(customer: Customer): Result<Unit>
    suspend fun deleteCustomer(customerId: String): Result<Unit>
    fun searchCustomers(query: String): Flow<List<Customer>>
    suspend fun seedDummyData(): Result<Int>
    fun getCustomerCountFlow(): Flow<Int>
}

class FirestoreCustomerRepository(
    private val collection: CollectionReference = FirebaseManager.customersCollection
) : CustomerRepository {

    override fun getCustomersFlow(): Flow<List<Customer>> = callbackFlow {
        val subscription = collection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val customers = snapshot.documents.mapNotNull { doc ->
                        doc.data?.let { data ->
                            Customer.fromMap(doc.id, data)
                        }
                    }
                    trySend(customers)
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getCustomerById(customerId: String): Result<Customer> {
        return try {
            val doc = collection.document(customerId).get().await()
            if (doc.exists() && doc.data != null) {
                Result.success(Customer.fromMap(doc.id, doc.data!!))
            } else {
                Result.failure(Exception("Customer with ID $customerId not found."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addCustomer(customer: Customer): Result<String> {
        return try {
            val docRef = if (customer.customerId.isNotBlank()) {
                collection.document(customer.customerId)
            } else {
                collection.document()
            }
            val preparedCustomer = customer.copy(
                customerId = docRef.id,
                createdAt = if (customer.createdAt > 0) customer.createdAt else System.currentTimeMillis()
            )
            docRef.set(preparedCustomer.toMap()).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCustomer(customer: Customer): Result<Unit> {
        return try {
            if (customer.customerId.isBlank()) {
                return Result.failure(IllegalArgumentException("Customer ID cannot be empty for update."))
            }
            collection.document(customer.customerId).set(customer.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCustomer(customerId: String): Result<Unit> {
        return try {
            if (customerId.isBlank()) {
                return Result.failure(IllegalArgumentException("Customer ID cannot be empty."))
            }
            collection.document(customerId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun searchCustomers(query: String): Flow<List<Customer>> {
        val trimmed = query.trim().lowercase()
        return getCustomersFlow().map { list ->
            if (trimmed.isEmpty()) {
                list
            } else {
                list.filter { customer ->
                    customer.fullName.lowercase().contains(trimmed) ||
                            customer.phoneNumber.lowercase().contains(trimmed) ||
                            customer.email.lowercase().contains(trimmed) ||
                            customer.address.lowercase().contains(trimmed)
                }
            }
        }
    }

    override suspend fun seedDummyData(): Result<Int> {
        return DummyDataSeeder.seedDatabase()
    }

    override fun getCustomerCountFlow(): Flow<Int> {
        return getCustomersFlow().map { it.size }
    }
}
