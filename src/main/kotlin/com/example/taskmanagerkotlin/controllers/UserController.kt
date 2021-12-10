package com.example.taskmanagerkotlin.controllers

import com.example.taskmanagerkotlin.Repository.UserRepository
import com.example.taskmanagerkotlin.dtos.ErrorsDTO
import com.example.taskmanagerkotlin.extensions.md5
import com.example.taskmanagerkotlin.extensions.toHex
import com.example.taskmanagerkotlin.models.User
import com.example.taskmanagerkotlin.validators.UserValidator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/user")
class UserController(val userRepository: UserRepository) {
    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<Any> {
        try {

            val errors = UserValidator(userRepository).paramsValidation(user)

            if (errors.size > 0) {
                return ResponseEntity(
                    ErrorsDTO(HttpStatus.BAD_REQUEST.value(), null, errors),
                    HttpStatus.BAD_REQUEST
                )
            }

            user.password = md5(user.password).toHex()

            userRepository.save(user)

            return ResponseEntity("User successfully created!", HttpStatus.CREATED)

        } catch (err: Exception) {
            return ResponseEntity(
                ErrorsDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create user, please try again."),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}

