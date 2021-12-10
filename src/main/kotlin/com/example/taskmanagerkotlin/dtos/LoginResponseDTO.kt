package com.example.taskmanagerkotlin.dtos

data class LoginResponseDTO (val name: String, val email: String, val token: String = "")