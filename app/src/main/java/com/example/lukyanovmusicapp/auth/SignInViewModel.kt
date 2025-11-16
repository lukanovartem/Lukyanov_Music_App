package com.example.lukyanovmusicapp.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log
import kotlin.text.isNotBlank

class SignInViewModel: ViewModel() {
    private val authRepository = AuthRepository()
    val signInState: MutableStateFlow<AuthResult> = MutableStateFlow(AuthResult.Empty)

    fun signIn(
        email: String,
        pass: String
    ) {
        viewModelScope.launch {
            signInState.value = authRepository.signIn(email, pass)
        }
    }
}