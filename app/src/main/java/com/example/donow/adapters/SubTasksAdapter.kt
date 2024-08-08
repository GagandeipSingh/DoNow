package com.example.donow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.donow.dataBase.SubTaskEntity
import com.example.donow.interFaces.SubTasksInterFace0
import com.example.donow.databinding.SubtasksLayoutBinding

class SubTasksAdapter(private val subTasksList : ArrayList<SubTaskEntity>, private val subTasksInterFace0: SubTasksInterFace0) : RecyclerView.Adapter<SubTasksAdapter.ViewHolder>() {
    class ViewHolder(val binding : SubtasksLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val taskBinding = SubtasksLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(taskBinding)
    }

    override fun getItemCount(): Int {
        return subTasksList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvString.text = subTasksList[position].subTask
        holder.binding.tvSubStr.text = subTasksList[position].description
        if(subTasksList[position].isFavourite){
            holder.binding.starEmpty.visibility = View.INVISIBLE
            holder.binding.star.visibility = View.VISIBLE
        }
        else{
            holder.binding.starEmpty.visibility = View.VISIBLE
            holder.binding.star.visibility = View.INVISIBLE
        }
        holder.binding.mark.setOnCheckedChangeListener{_,isChecked ->
            if(holder.binding.mark.isPressed)
            subTasksInterFace0.showDialog(isChecked,holder,position)
        }
        holder.binding.starEmpty.setOnClickListener {
            subTasksInterFace0.addFavourite(holder, position)
        }
        holder.binding.star.setOnClickListener {
            subTasksInterFace0.removeFavourite(holder, position)
        }
        holder.itemView.setOnClickListener {
            subTasksInterFace0.startDetails(subTasksList[position])
        }
    }
}