package com.example.donow.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface DaoInterFace {
    @Insert
    fun insertTask(taskEntity: TasksEntity): Long // Returns the ID of the inserted row

    @Insert
    fun insertSubTask(subTaskEntity: SubTaskEntity): Long // Returns the ID of the inserted row

    @Query("SELECT * FROM TasksEntity WHERE isCompleted = 0 or subTasksCompleted = 0")
    fun getList() : List<TasksEntity>

    @Query("SELECT * FROM SubTaskEntity WHERE taskId = :taskId and isCompleted = 0")
    fun getSubList(taskId : Int) : List<SubTaskEntity>

    @Query("SELECT * FROM TasksEntity WHERE isCompleted = 1")
    fun getListCompleted() : List<TasksEntity>

    @Query("SELECT * FROM SubTaskEntity WHERE isCompleted = 1 and taskId = :taskId")
    fun getSubListCompleted(taskId: Int) : List<SubTaskEntity>

    @Query("SELECT * FROM TasksEntity WHERE isFavourite = 1")
    fun getListFavorites() : List<TasksEntity>

    @Query("SELECT * FROM SubTaskEntity WHERE taskId = :taskId and isFavourite = 1")
    fun getSubListFavorites(taskId: Int) : List<SubTaskEntity>

    @Query("SELECT * FROM TasksEntity WHERE id = :tempId")
    fun getEntity(tempId: Long): TasksEntity

    @Query("SELECT * FROM SubTaskEntity WHERE id = :tempId")
    fun getSubEntity(tempId: Long): SubTaskEntity

    @Update
    fun updateTask(taskEntity: TasksEntity)

    @Update
    fun updateSubTask(subTaskEntity: SubTaskEntity)

    @Query("UPDATE TasksEntity SET subTasksCompleted = :completed WHERE id = :taskId")
    fun updateSubTasksCompleted(taskId: Int, completed: Boolean)

    @Query("UPDATE subtaskentity SET isCompleted = :completed WHERE taskId = :taskId")
    fun subTasksCompleted(taskId: Int, completed: Boolean)

    @Query("SELECT COUNT(*) FROM subtaskentity WHERE taskId = :taskId AND isCompleted = 0")
    fun getIncompleteSubtaskCount(taskId: Int): Int

    @Query("SELECT * FROM TasksEntity ORDER BY dateCreated ASC")
    fun getTaskEntitiesAsc(): List<TasksEntity>

    @Query("SELECT * FROM TasksEntity ORDER BY dateCreated DESC")
    fun getTaskEntitiesDesc(): List<TasksEntity>

    @Query("SELECT * FROM TasksEntity ORDER BY dateCompleted ASC")
    fun getTaskEntitiesAscC(): List<TasksEntity>

    @Query("SELECT * FROM TasksEntity ORDER BY dateCompleted DESC")
    fun getTaskEntitiesDescC(): List<TasksEntity>

    @Query("SELECT * FROM SubTaskEntity WHERE taskId = :taskId ORDER BY dateCreated ASC")
    fun getSubTaskEntitiesAsc(taskId: Int): List<SubTaskEntity>

    @Query("SELECT * FROM SubTaskEntity WHERE taskId = :taskId ORDER BY dateCreated DESC")
    fun getSubTaskEntitiesDesc(taskId: Int): List<SubTaskEntity>

    @Query("SELECT * FROM subtaskentity WHERE taskId = :taskId ORDER BY dateCompleted ASC")
    fun getSubTaskEntitiesAscC(taskId: Int): List<SubTaskEntity>

    @Query("SELECT * FROM SubTaskEntity WHERE taskId = :taskId ORDER BY dateCompleted DESC")
    fun getSubTaskEntitiesDescC(taskId: Int): List<SubTaskEntity>

    @Query("DELETE FROM subtaskentity WHERE id = :subTaskId")
    fun deleteSubTask(subTaskId : Long)

    @Query("DELETE FROM subtaskentity WHERE taskId = :taskId")
    fun deleteSubtasksByTaskId(taskId : Long)

    @Query("DELETE FROM tasksentity WHERE id = :taskId")
    fun deleteTask(taskId: Long)

    @Transaction
    fun deleteTaskAndSubtasks(taskId: Long) {
        deleteSubtasksByTaskId(taskId)
        deleteTask(taskId)
    }

}