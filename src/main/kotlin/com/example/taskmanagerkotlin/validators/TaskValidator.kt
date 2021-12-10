package com.example.taskmanagerkotlin.validators

import com.example.taskmanagerkotlin.Repository.TaskRepository
import com.example.taskmanagerkotlin.models.Task
import java.time.LocalDate

class TaskValidator(private val taskRepository: TaskRepository?) {
    fun validateTask(task: Task?): MutableList<String> {
        val errors = mutableListOf<String>()

        if (task == null) {
            errors.add("Invalid task params")
        } else {

            if (task.name.isNullOrBlank() || task.name.isNullOrEmpty() || task.name.length < 4)
                errors.add("Invalid name params")

            if (task.scheduledFinishDate.isBefore(LocalDate.now()))
                errors.add("Scheduled finish date must be ahead of the present moment")
        }
        return errors
    }

    fun validateUpdateTask(updateModel: Task?): MutableList<String> {
        val errors = mutableListOf<String>()

        if (updateModel == null) {
            errors.add("Invalid task params")
        } else {

            if (!updateModel.name.isNullOrEmpty() && !updateModel.name.isNullOrBlank() && updateModel.name.length < 4)
                errors.add("Invalid name params.")

            if (updateModel.scheduledFinishDate.isBefore(LocalDate.now()))
                errors.add("Scheduled finish date must be ahead of the present moment.")

            if (updateModel.finishDate != null && updateModel.finishDate == LocalDate.MIN)
                errors.add("Finish date is invalid.")
        }
        return errors
    }
}