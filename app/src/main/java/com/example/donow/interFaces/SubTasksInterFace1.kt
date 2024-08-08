package com.example.donow.interFaces

import com.example.donow.adapters.SubFavouritesAdapter
import com.example.donow.dataBase.SubTaskEntity

interface SubTasksInterFace1 {
    fun refreshSubFavourites()
    fun removeSubFavourites(holder: SubFavouritesAdapter.ViewHolder, position: Int)
    fun showDialog(isChecked : Boolean, holder : SubFavouritesAdapter.ViewHolder, position : Int)
    fun startDetails(entity : SubTaskEntity)
    fun notifyAdapter()
}