package com.example.donow.dataBase

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date


@Entity(foreignKeys = [ForeignKey(entity = TasksEntity::class,
    parentColumns = ["id"],
    childColumns = ["taskId"])])
data class SubTaskEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,
    var subTask : String? = null,
    var description : String? = null,
    var isCompleted : Boolean = false,
    var isFavourite : Boolean = false,
    var taskId : Int ? = 0,
    var dateCreated : Date? = Calendar.getInstance().time,
    var dateCompleted : Date?
)