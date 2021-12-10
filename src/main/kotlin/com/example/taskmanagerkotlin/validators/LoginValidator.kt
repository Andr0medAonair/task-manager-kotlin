package com.example.taskmanagerkotlin.validators

import com.example.taskmanagerkotlin.dtos.LoginDTO

class LoginValidator {
    fun validateDto(dto: LoginDTO): MutableList<String> {
        val errors = mutableListOf<String>()

        if (dto.login.isNullOrBlank() || dto.login.isNullOrEmpty())
            errors.add("Invalid login params")

        if (dto.password.isNullOrBlank() || dto.password.isNullOrEmpty())
            errors.add("Invalid password params")

        return errors
    }
}