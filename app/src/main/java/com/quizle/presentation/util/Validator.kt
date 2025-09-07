package com.quizle.presentation.util

import android.content.Context
import com.quizle.R
import org.koin.compose.koinInject
import org.koin.dsl.koinApplication


class Validator(
    private val context: Context
) {

    private val errors = mutableMapOf<String, String?>()

    fun validateUserName(name: String): String? {
        val error = if (name.isEmpty()) {
            context.getString(R.string.validation_name_empty)
        } else if (name.length < 2) {
            context.getString(R.string.validation_name_too_short)
        } else {
            null
        }
        errors["username"] = error
        return error
    }

    fun validateEmail(email: String): String? {
        val error = if (email.isEmpty()) {
            context.getString(R.string.validation_email_empty)
        } else {
            val emailRegex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}".toRegex()
            if (!email.matches(emailRegex)) {
                context.getString(R.string.validation_email_invalid)
            } else {
                null
            }
        }
        errors["email"] = error
        return error
    }

    fun validatePassword(password: String): String? {
        val error = if (password.isEmpty()) {
            context.getString(R.string.validation_password_empty)
        } else if (password.length < 8) {
            context.getString(R.string.validation_password_too_short)
        } else {
            null
        }
        errors["password"] = error
        return error
    }

    fun validateConfirmPassword(confirmPassword: String, originalPassword: String): String? {
        val error = if (confirmPassword.isEmpty()) {
            context.getString(R.string.validation_confirm_password_empty)
        } else if (confirmPassword != originalPassword) {
            context.getString(R.string.validation_passwords_no_match)
        } else {
            null
        }
        errors["confirm_password"] = error
        return error
    }

    fun isAllFieldValidate(): Boolean {
        return errors.all { it.value == null }
    }
}

