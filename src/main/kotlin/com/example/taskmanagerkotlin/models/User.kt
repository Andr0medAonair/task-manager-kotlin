package com.example.taskmanagerkotlin.models

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*

@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String = "",
    val email: String = "",
    var password: String = "",

    @JsonBackReference
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    val tasks: List<Task> = emptyList()
) {

}