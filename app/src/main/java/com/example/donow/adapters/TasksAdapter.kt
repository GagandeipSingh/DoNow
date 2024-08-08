package com.example.donow.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.donow.dataBase.TasksEntity
import com.example.donow.interFaces.InteractionInterFace0
import com.example.donow.databinding.TasksLayoutBinding

class TasksAdapter(private val tasksList : ArrayList<TasksEntity>, private val interactionInterFace0: InteractionInterFace0) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    class ViewHolder (val binding : TasksLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val taskBinding = TasksLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(taskBinding)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(!tasksList[position].isCompleted){
            holder.binding.tvString.paintFlags = 0
            holder.binding.tvSubStr.paintFlags = 0
            holder.binding.mark.isChecked = false
        }
        else{
            holder.binding.tvString.paintFlags = holder.binding.tvString.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.tvSubStr.paintFlags = holder.binding.tvSubStr.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.mark.isChecked = true
        }
        if(tasksList[position].subTasksCompleted){
            holder.binding.subTasks.paintFlags = holder.binding.subTasks.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        else {
            holder.binding.subTasks.paintFlags = 0
        }
        holder.binding.tvString.text = tasksList[position].task
        holder.binding.tvSubStr.text = tasksList[position].description
        if(tasksList[position].isFavourite){
            holder.binding.starEmpty.visibility = View.INVISIBLE
            holder.binding.star.visibility = View.VISIBLE
        }
        else{
            holder.binding.starEmpty.visibility = View.VISIBLE
            holder.binding.star.visibility = View.INVISIBLE
        }
        val id = tasksList[position].id.toString().toInt()
        holder.binding.mark.setOnCheckedChangeListener{_,isChecked ->
            if(holder.binding.mark.isPressed)
            interactionInterFace0.showDialog(isChecked,holder,position, id)
        }
        holder.binding.star.setOnClickListener {
            interactionInterFace0.removeFavourite(holder, position)
        }
        holder.binding.starEmpty.setOnClickListener {
            interactionInterFace0.addFavourite(holder, position)
        }
        holder.binding.subTasks.setOnClickListener {
            val taskName = tasksList[position].task.toString()
            interactionInterFace0.startActivity(taskName,id)
        }
        holder.itemView.setOnClickListener {
            interactionInterFace0.startDetails(tasksList[position])
        }
    }
}