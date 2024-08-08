package com.example.donow.interFaces

import com.example.donow.adapters.TasksAdapter
import com.example.donow.dataBase.TasksEntity

interface InteractionInterFace0 {
    fun addedLast()
    fun refreshTasks()
    fun showDialog(isChecked : Boolean, holder : TasksAdapter.ViewHolder, position : Int, taskId : Int)
    fun addFavourite(holder: TasksAdapter.ViewHolder, position: Int)
    fun removeFavourite(holder: TasksAdapter.ViewHolder, position: Int)
    fun startActivity(taskName : String, id : Int)
    fun startDetails(entity : TasksEntity)
    fun notifyAdapter()
}