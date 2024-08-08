package com.example.donow.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.donow.dataBase.TasksEntity
import com.example.donow.interFaces.InteractionInterFace1
import com.example.donow.databinding.TasksLayoutBinding

class FavouritesAdapter(private val favouritesList : ArrayList<TasksEntity>, private val interactionInterFace1: InteractionInterFace1):RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {
    class ViewHolder(val binding : TasksLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val tasksBinding = TasksLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(tasksBinding)
    }

    override fun getItemCount(): Int {
        return favouritesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(!favouritesList[position].isCompleted){
            holder.binding.tvString.paintFlags = 0
            holder.binding.tvSubStr.paintFlags = 0
            holder.binding.mark.isChecked = false
        }
        else{
            holder.binding.tvString.paintFlags = holder.binding.tvString.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.tvSubStr.paintFlags = holder.binding.tvSubStr.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.mark.isChecked = true
        }
        if(favouritesList[position].subTasksCompleted){
            holder.binding.subTasks.paintFlags = holder.binding.subTasks.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        else {
            holder.binding.subTasks.paintFlags = 0
        }
        holder.binding.tvString.text = favouritesList[position].task
        holder.binding.tvSubStr.text = favouritesList[position].description
        holder.binding.starEmpty.visibility = View.INVISIBLE
        holder.binding.star.visibility = View.VISIBLE
        holder.binding.star.setOnClickListener {
            interactionInterFace1.removeFavourite(holder, position)
        }
        holder.binding.mark.setOnCheckedChangeListener{_,isChecked ->
            val id = favouritesList[position].id.toString().toInt()
            if(holder.binding.mark.isPressed)
                interactionInterFace1.showDialog(isChecked,holder,position,id)
        }
        holder.binding.subTasks.setOnClickListener {
            val taskName = favouritesList[position].task.toString()
            val id = favouritesList[position].id.toString().toInt()
            interactionInterFace1.startActivity(taskName,id)
        }
        holder.itemView.setOnClickListener {
            interactionInterFace1.startDetails(favouritesList[position])
        }
    }
}