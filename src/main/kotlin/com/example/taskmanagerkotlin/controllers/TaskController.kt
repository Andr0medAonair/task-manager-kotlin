package com.example.taskmanagerkotlin.controllers

import com.example.taskmanagerkotlin.Repository.TaskRepository
import com.example.taskmanagerkotlin.Repository.UserRepository
import com.example.taskmanagerkotlin.dtos.ErrorsDTO
import com.example.taskmanagerkotlin.dtos.SuccessDTO
import com.example.taskmanagerkotlin.models.Task
import com.example.taskmanagerkotlin.validators.TaskValidator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.Period
import java.util.*

@RestController
@RequestMapping("/api/task")
class TaskController(userRepository: UserRepository, val taskRepository: TaskRepository) :
    BaseController(userRepository) {

    @GetMapping
    fun getTasks(
        @RequestHeader("Authorization") authorization: String,
        @RequestParam beginPeriod: Optional<String>,
        @RequestParam endPeriod: Optional<String>,
        @RequestParam status: Optional<Int>
    ): ResponseEntity<Any> {
        try {
            val user = readToken(authorization)

            var beginPeriodDt = if(beginPeriod.isPresent && beginPeriod.get().isNotEmpty()){
                LocalDate.parse(beginPeriod.get())
            }else{
                null
            }

            var endPeriodDt = if(endPeriod.isPresent && endPeriod.get().isNotEmpty()){
                LocalDate.parse(endPeriod.get())
            }else{
                null
            }

            var statusInt = if(status.isPresent){
                status.get()
            }else{
                0
            }

            val result = taskRepository.filteredFindByUser(user.id, beginPeriodDt, endPeriodDt, statusInt)
            return ResponseEntity(result, HttpStatus.OK)
        } catch (err: Exception) {
            return ResponseEntity(
                ErrorsDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error encountered while importing tasks. Please, try again later!"
                ), HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    @PostMapping
    fun addTask(@RequestBody req: Task, @RequestHeader("Authorization") authorization: String): ResponseEntity<Any> {
        try {
            var user = readToken(authorization)
            val paramErrors = TaskValidator(taskRepository).validateTask(req)

            if (paramErrors.size > 0) {
                return ResponseEntity(
                    ErrorsDTO(HttpStatus.BAD_REQUEST.value(), null, paramErrors),
                    HttpStatus.BAD_REQUEST
                )
            }

            val task = Task(
                name = req.name,
                scheduledFinishDate = req.scheduledFinishDate,
                user = user
            )

            taskRepository.save(task)

            return ResponseEntity(
                SuccessDTO(
                    "Task created successfully."
                ), HttpStatus.CREATED
            )
        } catch (err: Exception) {
            return ResponseEntity(
                ErrorsDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error encountered while creating task. Please, try again later!"
                ), HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long, @RequestHeader("Authorization") authorization: String): ResponseEntity<Any> {
        try {
            val user = readToken(authorization)
            val task = taskRepository?.findByIdOrNull(id)

            if (task == null || task.user?.id != user.id) {
                return ResponseEntity(
                    ErrorsDTO(HttpStatus.BAD_REQUEST.value(), "Task does not exist.", null),
                    HttpStatus.BAD_REQUEST
                )
            }

            taskRepository.delete(task)

            return ResponseEntity(SuccessDTO("Task deleted successfully"), HttpStatus.OK)
        } catch (err: Exception) {
            return ResponseEntity(
                ErrorsDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error encountered while deleting task. Please, try again later!"
                ), HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @RequestBody updateModel: Task,
        @RequestHeader authorization: String
    ): ResponseEntity<Any> {
        try {
            val user = readToken(authorization)
            var task = taskRepository.findByIdOrNull(id)

            if (user == null || task == null) {
                return ResponseEntity(
                    ErrorsDTO(HttpStatus.BAD_REQUEST.value(), "Task does not exist.", null),
                    HttpStatus.BAD_REQUEST
                )
            }

            val paramErrors = TaskValidator(taskRepository).validateUpdateTask(updateModel)

            if (paramErrors.size > 0) {
                return ResponseEntity(
                    ErrorsDTO(HttpStatus.BAD_REQUEST.value(), null, paramErrors),
                    HttpStatus.BAD_REQUEST
                )
            }

            if (!updateModel.name.isNullOrEmpty() && !updateModel.name.isNullOrBlank())
                task.name = updateModel.name

//            if(!updateModel.dataPrevistaConclusao.isBefore(LocalDate.now())){
//                tarefa.dataPrevistaConclusao = updateModel.dataPrevistaConclusao
//            }

            if (!updateModel.scheduledFinishDate.isBefore(LocalDate.now()))
                task.scheduledFinishDate = updateModel.scheduledFinishDate

            if (updateModel.finishDate != null && updateModel.finishDate != LocalDate.MIN)
                task.finishDate = updateModel.finishDate

            taskRepository.save(task)

            return ResponseEntity(SuccessDTO("Task updated successfully"), HttpStatus.OK)
        } catch (err: Exception) {
            return ResponseEntity(
                ErrorsDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error encountered while updating task. Please, try again later!"
                ), HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}