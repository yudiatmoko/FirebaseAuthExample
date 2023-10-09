package com.catnip.firebaseauthexample.data.network.auth

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface FirebaseAuthDataSource {
    fun isLoggedIn(): Boolean
    fun doLogout(): Boolean
    fun getCurrentUser(): FirebaseUser?

    suspend fun doRegister(
        fullName: String,
        email: String,
        password: String,
    ) : Boolean

    suspend fun doLogin(
        email: String,
        password: String,
    ) : Boolean
}

class FirebaseAuthDataSourceImpl(
    private val firebaseAuth: FirebaseAuth
): FirebaseAuthDataSource{
    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun doLogout(): Boolean {
        Firebase.auth.signOut()
        return true
    }

    override fun getCurrentUser(): FirebaseUser {
        TODO("Not yet implemented")
    }

    override suspend fun doRegister(
        fullName: String,
        email: String,
        password: String,
    ): Boolean {
        val registerResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        registerResult.user?.updateProfile(
            userProfileChangeRequest {
                displayName = fullName
            }
        )?.await()
        return registerResult.user != null
    }

    override suspend fun doLogin(
        email: String,
        password: String,
    ): Boolean {
        val registerResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return registerResult.user != null
    }
}