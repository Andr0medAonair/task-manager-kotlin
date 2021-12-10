package com.example.taskmanagerkotlin.dtos

class ErrorsDTO (val status: Int, val error: String? = null, val errors: List<String>? = null)