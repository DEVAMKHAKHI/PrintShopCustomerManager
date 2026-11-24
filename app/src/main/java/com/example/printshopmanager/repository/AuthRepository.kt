package com.example.printshopmanager.repository

import com.example.printshopmanager.firebase.FirebaseManager
import com.example.printshopmanager.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String, displayName: String = ""): Result<User>
    suspend fun sendPasswordReset(email: String): Result<Unit>
    fun logout()
    fun getCurrentUser(): User?
    fun isUserLoggedIn(): Boolean
    val authStateFlow: Flow<User?>
}

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseManager.auth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email.trim(), password).await()
            val firebaseUser = result.user ?: throw Exception("Authentication returned null user.")
            Result.success(
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email.orEmpty(),
                    displayName = firebaseUser.displayName.orEmpty().ifEmpty {
                        firebaseUser.email?.substringBefore("@").orEmpty()
                    }
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, displayName: String): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
            val firebaseUser = result.user ?: throw Exception("Failed to create user.")

            if (displayName.isNotBlank()) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName.trim())
                    .build()
                firebaseUser.updateProfile(profileUpdates).await()
            }

            Result.success(
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email.orEmpty(),
                    displayName = displayName.ifEmpty { firebaseUser.email?.substringBefore("@").orEmpty() }
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email.trim()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        auth.signOut()
    }

    override fun getCurrentUser(): User? {
        val user = auth.currentUser ?: return null
        return User(
            uid = user.uid,
            email = user.email.orEmpty(),
            displayName = user.displayName.orEmpty().ifEmpty {
                user.email?.substringBefore("@").orEmpty()
            }
        )
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override val authStateFlow: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                trySend(
                    User(
                        uid = user.uid,
                        email = user.email.orEmpty(),
                        displayName = user.displayName.orEmpty().ifEmpty {
                            user.email?.substringBefore("@").orEmpty()
                        }
                    )
                )
            } else {
                trySend(null)
            }
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }
}
