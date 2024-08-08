package com.example.donow.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date

@Entity
data class TasksEntity(
    @PrimaryKey (autoGenerate = true)
    var id : Long = 0,
    var task : String ? = null,
    var description : String ? = null,
    var isCompleted : Boolean = false,
    var isFavourite : Boolean = false,
    var subTasksCompleted : Boolean = false,
    var dateCreated : Date?= Calendar.getInstance().time,
    var dateCompleted : Date?
)