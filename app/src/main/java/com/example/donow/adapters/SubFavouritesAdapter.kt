package com.example.donow.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.donow.dataBase.SubTaskEntity
import com.example.donow.interFaces.SubTasksInterFace1
import com.example.donow.databinding.SubtasksLayoutBinding

class SubFavouritesAdapter(private val subFavouritesList : ArrayList<SubTaskEntity>, private val subTasksInterFace1: SubTasksInterFace1):RecyclerView.Adapter<SubFavouritesAdapter.ViewHolder>() {
    class ViewHolder(val binding : SubtasksLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val tasksBinding = SubtasksLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(tasksBinding)
    }

    override fun getItemCount(): Int {
        return subFavouritesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(!subFavouritesList[position].isCompleted){
            holder.binding.tvString.paintFlags = 0
            holder.binding.tvSubStr.paintFlags = 0
            holder.binding.mark.isChecked = false
        }
        else{
            holder.binding.tvString.paintFlags = holder.binding.tvString.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.tvSubStr.paintFlags = holder.binding.tvSubStr.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.mark.isChecked = true
        }
        holder.binding.tvString.text = subFavouritesList[position].subTask
        holder.binding.tvSubStr.text = subFavouritesList[position].description
        holder.binding.starEmpty.visibility = View.INVISIBLE
        holder.binding.star.visibility = View.VISIBLE
        holder.binding.star.setOnClickListener {
            subTasksInterFace1.removeSubFavourites(holder, position)
        }
        holder.binding.mark.setOnCheckedChangeListener{_,isChecked ->
            if(holder.binding.mark.isPressed)
                subTasksInterFace1.showDialog(isChecked,holder,position)
        }
        holder.itemView.setOnClickListener {
            subTasksInterFace1.startDetails(subFavouritesList[position])
        }
    }
}