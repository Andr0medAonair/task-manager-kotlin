package com.example.taskmanagerkotlin.validators

import com.example.taskmanagerkotlin.Repository.UserRepository
import com.example.taskmanagerkotlin.models.User

class UserValidator(private val userRepository: UserRepository) {

    fun paramsValidation(user: User?): MutableList<String> {
        val errors = mutableListOf<String>()

        if (user == null) {
            errors.add("Invalid user params.")
        }
        else{
            if (user.name.isNullOrEmpty() || user.name.isNullOrBlank() || user.name.length < 3) {
                errors.add("Invalid name.")
            }

            if (user.email.isNullOrEmpty() || user.email.isNullOrBlank() || user.email.length < 5) {
                errors.add("Invalid email.")
            }

            if (user.password.isNullOrEmpty() || user.password.isNullOrBlank() || user.password.length < 5) {
                errors.add("Invalid password.")
            }

            if (userRepository.findByEmail(user.email) != null) {
                errors.add("User already exists.")
            }
        }

        return errors
    }
}