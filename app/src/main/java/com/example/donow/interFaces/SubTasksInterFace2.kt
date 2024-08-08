package com.example.donow.interFaces

import com.example.donow.adapters.SubCompletedAdapter
import com.example.donow.dataBase.SubTaskEntity

interface SubTasksInterFace2 {
    fun removeFavourite(holder : SubCompletedAdapter.ViewHolder, position : Int)
    fun addFavourite(holder : SubCompletedAdapter.ViewHolder, position : Int)
    fun refreshSubCompleted()
    fun showDialog(isChecked : Boolean, holder : SubCompletedAdapter.ViewHolder, position : Int)
    fun startDetails(entity : SubTaskEntity)
    fun notifyAdapter()
}