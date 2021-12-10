package com.example.taskmanagerkotlin.models

import com.fasterxml.jackson.annotation.JsonBackReference
import java.time.LocalDate
import javax.persistence.*

@Entity
data class Task(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
    var name: String = "",
    var scheduledFinishDate: LocalDate = LocalDate.MIN,
    var finishDate: LocalDate? = null,
    val text: String? = null,

    PURUVOODOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    val user: User? = null
) {

}