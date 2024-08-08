package com.example.donow.interFaces

import com.example.donow.dataBase.SubTaskEntity
import com.example.donow.adapters.SubTasksAdapter

interface SubTasksInterFace0 {
    fun refreshSubTasks()
    fun notifyAdapter()
    fun addFavourite(holder: SubTasksAdapter.ViewHolder, position : Int)
    fun removeFavourite(holder : SubTasksAdapter.ViewHolder, position : Int)
    fun showDialog(isChecked : Boolean, holder : SubTasksAdapter.ViewHolder, position : Int)
    fun startDetails(entity : SubTaskEntity)
}