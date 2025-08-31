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


//class Validator(
//    private val context: Context
//) {
//    private val errors = mutableMapOf<String, String?>()
//
//
//    fun validateUserName(name: String): String? {
//        val error = if (name.isEmpty()) {
//            "Name can't be empty."
//        } else if (name.length < 2) {
//            "Name must be at least 2 characters long."
//        } else {
//            null // No error
//        }
//        errors["username"] = error // Store the result
//        return error
//    }
//
//    fun validateEmail(email: String): String? {
//        val error = if (email.isEmpty()) {
//            "Email can't be empty."
//        } else {
//            val emailRegex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}".toRegex()
//            if (!email.matches(emailRegex)) {
//                "Invalid email format."
//            } else {
//                null // No error
//            }
//        }
//        errors["email"] = error // Store the result
//        return error
//    }
//
//    fun validatePassword(password: String): String? {
//        val error = if (password.isEmpty()) {
//            "Password can't be empty."
//        } else if (password.length < 8) {
//            "Password must be at least 8 characters long."
//        } else {
//            null // No error
//        }
//        errors["password"] = error // Store the result
//        return error
//    }
//
//    fun validateConfirmPassword(confirmPassword: String, originalPassword: String): String? {
//        val error = if (confirmPassword.isEmpty()) {
//            "Confirm Password can't be empty."
//        } else if (confirmPassword != originalPassword) {
//            "Passwords don't match."
//        } else {
//            null // No error
//        }
//        errors["confirm_password"] = error // Store the result
//        return error
//    }
//
//
//    fun isAllFieldValidate(): Boolean {
//        return errors.all { it.value == null }
//    }
//
//
//
//
//}