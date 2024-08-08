package com.example.donow.interFaces

import com.example.donow.adapters.FavouritesAdapter
import com.example.donow.dataBase.TasksEntity

interface InteractionInterFace1 {
    fun refreshFavourites()
    fun addedLast()
    fun removeFavourite(holder: FavouritesAdapter.ViewHolder, position: Int)
    fun showDialog(isChecked : Boolean, holder: FavouritesAdapter.ViewHolder, position: Int, taskId : Int)
    fun startActivity(taskName : String, id : Int)
    fun startDetails(entity : TasksEntity)
    fun notifyAdapter()
}