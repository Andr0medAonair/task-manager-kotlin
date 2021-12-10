package com.example.taskmanagerkotlin.controllers

import com.example.taskmanagerkotlin.Repository.UserRepository
import com.example.taskmanagerkotlin.dtos.ErrorsDTO
import com.example.taskmanagerkotlin.dtos.LoginDTO
import com.example.taskmanagerkotlin.dtos.LoginResponseDTO
import com.example.taskmanagerkotlin.extensions.md5
import com.example.taskmanagerkotlin.extensions.toHex
import com.example.taskmanagerkotlin.utils.JWTUtils
import com.example.taskmanagerkotlin.validators.LoginValidator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("api/login")
class LoginController(val userRepository: UserRepository) {
    @PostMapping
    fun login(@RequestBody dto: LoginDTO): ResponseEntity<Any> {
        try {
            val paramErrors = LoginValidator().validateDto(dto)

            if (paramErrors.size > 0) {
                return ResponseEntity(
                    ErrorsDTO(HttpStatus.BAD_REQUEST.value(), null, paramErrors),
                    HttpStatus.BAD_REQUEST
                )
            }

            val user = userRepository.findByEmail(dto.login)

            if (user == null || user.password != md5(dto.password).toHex()) {
                return ResponseEntity(
                    ErrorsDTO(HttpStatus.BAD_REQUEST.value(), "Invalid user or password"),
                    HttpStatus.BAD_REQUEST
                )
            }

            val token = JWTUtils().generateToken(user.id.toString())
            val foundUser = LoginResponseDTO(user.name, user.email, token)

            return ResponseEntity(foundUser, HttpStatus.OK)

        } catch (err: Exception) {
            return ResponseEntity(
                ErrorsDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "There was an error logging in, please try again."
                ), HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}