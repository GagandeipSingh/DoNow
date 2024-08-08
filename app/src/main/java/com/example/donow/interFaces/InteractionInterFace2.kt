package com.example.donow.interFaces

import com.example.donow.adapters.CompletedAdapter
import com.example.donow.dataBase.TasksEntity

interface InteractionInterFace2 {
    fun refreshCompleted()
    fun removeFavourite(holder: CompletedAdapter.ViewHolder, position: Int)
    fun addFavourite(holder: CompletedAdapter.ViewHolder, position: Int)
    fun showDialog(isChecked : Boolean, holder : CompletedAdapter.ViewHolder, position : Int, taskId : Int)
    fun startActivity(taskName : String, id : Int)
    fun startDetails(entity : TasksEntity)
    fun notifyAdapter()
}