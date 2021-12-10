package com.example.taskmanagerkotlin.controllers

import com.example.taskmanagerkotlin.Repository.UserRepository
import com.example.taskmanagerkotlin.filters.UserFilter
import com.example.taskmanagerkotlin.models.User
import org.springframework.data.repository.findByIdOrNull

open class BaseController(private val userRepository: UserRepository) {
    fun readToken(auth: String): User {
        val token = auth.substring(7)
        val userId = UserFilter().getUserId(token)
        if (userId == null || userId.isNullOrBlank() || userId.isNullOrEmpty())
            throw IllegalArgumentException("You don't have access to this resource.")

        return userRepository.findByIdOrNull(userId.toLong()) ?: throw IllegalArgumentException("User not found.")
    }
}