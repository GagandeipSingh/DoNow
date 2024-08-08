package com.example.donow.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.donow.dataBase.SubTaskEntity
import com.example.donow.interFaces.SubTasksInterFace2
import com.example.donow.databinding.SubtasksLayoutBinding

class SubCompletedAdapter(private var list : ArrayList<SubTaskEntity>, private val subTasksInterFace2: SubTasksInterFace2) : RecyclerView.Adapter<SubCompletedAdapter.ViewHolder>(){
    class ViewHolder (val binding : SubtasksLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SubtasksLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvString.paintFlags = holder.binding.tvString.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.tvString.text = list[position].subTask
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
        holder.binding.mark.setOnCheckedChangeListener{_,isChecked ->
            subTasksInterFace2.showDialog(isChecked,holder,position)
        }
        holder.binding.star.setOnClickListener {
            subTasksInterFace2.removeFavourite(holder,position)
        }
        holder.binding.starEmpty.setOnClickListener {
            subTasksInterFace2.addFavourite(holder,position)
        }
        holder.itemView.setOnClickListener {
            subTasksInterFace2.startDetails(list[position])
        }
    }

}