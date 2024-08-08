package com.example.donow.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.donow.dataBase.TasksEntity
import com.example.donow.interFaces.InteractionInterFace2
import com.example.donow.databinding.TasksLayoutBinding

class CompletedAdapter(private var list : ArrayList<TasksEntity>, private val interactionInterFace2: InteractionInterFace2) : RecyclerView.Adapter<CompletedAdapter.ViewHolder>(){
    class ViewHolder (val binding : TasksLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TasksLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvString.paintFlags = holder.binding.tvString.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.tvString.text = list[position].task
        if(list[position].subTasksCompleted){
            holder.binding.subTasks.paintFlags = holder.binding.subTasks.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        else{
            holder.binding.subTasks.paintFlags = 0
        }
        if(list[position].isFavourite){
            holder.binding.starEmpty.visibility = View.INVISIBLE
            holder.binding.star.visibility = View.VISIBLE
        }
        else{
            holder.binding.starEmpty.visibility = View.VISIBLE
            holder.binding.star.visibility = View.INVISIBLE
        }
        holder.binding.tvSubStr.paintFlags = holder.binding.tvSubStr.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.tvSubStr.text = list[position].description
        holder.binding.mark.isChecked = list[position].isCompleted
        val id = list[position].id.toString().toInt()
        holder.binding.mark.setOnCheckedChangeListener{_,isChecked ->
            interactionInterFace2.showDialog(isChecked,holder,position,id)
        }
        holder.binding.star.setOnClickListener {
            interactionInterFace2.removeFavourite(holder,position)
        }
        holder.binding.starEmpty.setOnClickListener {
            interactionInterFace2.addFavourite(holder,position)
        }
        holder.binding.subTasks.setOnClickListener {
            val taskName = list[position].task.toString()
            interactionInterFace2.startActivity(taskName,id)
        }
        holder.itemView.setOnClickListener {
            interactionInterFace2.startDetails(list[position])
        }
    }
}