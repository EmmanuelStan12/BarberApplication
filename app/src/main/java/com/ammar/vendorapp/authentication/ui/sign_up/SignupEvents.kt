package com.ammar.vendorapp.authentication.ui.sign_up

import android.util.Patterns
import com.ammar.vendorapp.authentication.data.models.UserRegister
import com.ammar.vendorapp.authentication.ui.login.LoginEvents
import java.util.regex.Matcher
import java.util.regex.Pattern

sealed class SignupEvents {
    data class ChangeEmail(val email: String) : SignupEvents() {
        val error: String
            get() = if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) "" else "Email address not valid"
    }

    data class ChangePassword(val password: String) : SignupEvents() {

        private fun isValidPassword(password: String): Boolean {
            val pattern: Pattern
            val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
            pattern = Pattern.compile(passwordPattern)
            val matcher: Matcher = pattern.matcher(password)
            return matcher.matches() && password.length >= 8
        }
        val error: String
            get() = if (isValidPassword(password)) "" else "Password must contain at least 1 Number, 1 special character, 1 uppercase letter and must be at least 8 characters long"

    }

    data class ChangeUsername(val value: String) : SignupEvents() {
        val error: String
            get() = if (value.isEmpty()) "Username cannot be empty" else ""
    }

    data class ChangeFirstname(val value: String) : SignupEvents() {
        val error: String
            get() = if (value.isEmpty()) "First name cannot be empty" else ""
    }

    data class ChangeLastname(val value: String) : SignupEvents() {
        val error: String
            get() = if (value.isEmpty()) "Last name cannot be empty" else ""
    }

    object Register : SignupEvents()
}
