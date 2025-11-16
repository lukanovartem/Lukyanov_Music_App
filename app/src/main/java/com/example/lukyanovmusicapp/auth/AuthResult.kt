package com.example.lukyanovmusicapp.auth

sealed class AuthResult {
    object Success: AuthResult()
    object Loading: AuthResult()
    object Empty: AuthResult()
    data class Error(
        var error: String
    ): AuthResult()
}