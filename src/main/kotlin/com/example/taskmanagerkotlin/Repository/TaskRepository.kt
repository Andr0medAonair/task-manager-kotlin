package com.example.taskmanagerkotlin.Repository

import com.example.taskmanagerkotlin.models.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TaskRepository : JpaRepository<Task, Long> {

    @Query(
        "SELECT t FROM Task t " +
                " WHERE t.user.id  = :idUser " +
                "   AND (:beginPeriodDt IS NULL OR t.scheduledFinishDate >= :beginPeriodDt) " +
                "   AND (:endPeriodDt IS NULL OR t.scheduledFinishDate <= :endPeriodDt) " +
                "   AND (:status = 0 OR (:status = 1 AND t.finishDate IS NULL) " +
                "           OR (:status = 2 AND t.finishDate IS NOT NULL)) "
    )

    fun filteredFindByUser(idUser: Long, beginPeriodDt: LocalDate?, endPeriodDt: LocalDate?, status: Int): List<Task>?

}