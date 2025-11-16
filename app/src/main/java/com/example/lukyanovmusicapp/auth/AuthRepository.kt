package com.example.lukyanovmusicapp.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialProvider
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import java.security.AuthProvider

class AuthRepository {
    val auth = FirebaseAuth.getInstance()
    val currentUser = mutableStateOf(auth.currentUser)

    init {
        auth.addAuthStateListener {
            currentUser.value = auth.currentUser
        }
    }

    suspend fun signUp(email: String, pass: String): AuthResult {
        return try {
            auth.createUserWithEmailAndPassword(email, pass).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.toString())
        }
    }

    suspend fun signIn(email: String, pass: String): AuthResult {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.toString())
        }
    }

    fun signOut(): AuthResult {
        return try {
            auth.signOut()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.toString())
        }
    }

    suspend fun deleteAccount(
        email: String,
        pass: String
    ): AuthResult {
        return try {
            val credential = EmailAuthProvider.getCredential(email, pass)
            auth.currentUser?.reauthenticate(credential)?.await()
            auth.currentUser?.delete()?.await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.toString())
        }
    }
}