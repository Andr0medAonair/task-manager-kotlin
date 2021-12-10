package com.example.taskmanagerkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class TaskManagerKotlinApplication

fun main(args: Array<String>) {
	runApplication<TaskManagerKotlinApplication>(*args)
}
