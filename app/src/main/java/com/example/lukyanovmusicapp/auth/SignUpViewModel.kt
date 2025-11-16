package com.example.lukyanovmusicapp.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel: ViewModel() {
    private val authRepository = AuthRepository()

    val signUpState: MutableStateFlow<AuthResult> = MutableStateFlow(AuthResult.Empty)

    fun createUser(email: String, pass: String) {
        viewModelScope.launch {
            signUpState.value = authRepository.signUp(email, pass)
        }
    }

}