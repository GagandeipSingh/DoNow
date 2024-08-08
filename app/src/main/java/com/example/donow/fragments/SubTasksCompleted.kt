package com.example.donow.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donow.activities.DetailsSubTask
import com.example.donow.activities.SubTasks
import com.example.donow.adapters.SubCompletedAdapter
import com.example.donow.dataBase.SubTaskEntity
import com.example.donow.dataBase.TasksDatabase
import com.example.donow.interFaces.SubTasksInterFace2
import com.example.donow.databinding.FragmentSubTasksCompletedBinding
import com.google.gson.Gson

class SubTasksCompleted : Fragment(), SubTasksInterFace2 {
    private lateinit var binding : FragmentSubTasksCompletedBinding
    private lateinit var subTasks: SubTasks
    private lateinit var subCompletedAdapter: SubCompletedAdapter
    private lateinit var tasksDatabase : TasksDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subTasks = activity as SubTasks
        subTasks.subTasksInterFace2 = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSubTasksCompletedBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksDatabase = TasksDatabase.getInstance(requireContext())
        refreshSubCompleted()
        subCompletedAdapter = SubCompletedAdapter(subTasks.completedSubList,subTasks.subTasksInterFace2)
        binding.tasks.adapter = subCompletedAdapter
        binding.tasks.layoutManager = LinearLayoutManager(requireContext())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val incompleteSubtaskCount = tasksDatabase.daoInterface().getIncompleteSubtaskCount(subTasks.id)
        val isAllSubtasksCompleted = incompleteSubtaskCount == 0
        tasksDatabase.daoInterface().updateSubTasksCompleted(subTasks.id, isAllSubtasksCompleted)
        subTasks.getSubDataCompleted()
        subCompletedAdapter.notifyDataSetChanged()
        refreshSubCompleted()
    }

    override fun refreshSubCompleted() {
        if(subTasks.completedSubList.size == 0){
            binding.noTasks.visibility = View.VISIBLE
            binding.completedTasks.visibility = View.VISIBLE
        }
        else{
            binding.noTasks.visibility = View.GONE
            binding.completedTasks.visibility = View.GONE
        }
    }

    override fun removeFavourite(holder: SubCompletedAdapter.ViewHolder, position: Int) {
        holder.binding.star.visibility = View.INVISIBLE
        holder.binding.starEmpty.visibility = View.VISIBLE
        val subTasksEntity = subTasks.completedSubList[position]
        subTasksEntity.isFavourite = false
        tasksDatabase.daoInterface().updateSubTask(subTasksEntity)
        subTasks.getSubFavourites()
    }

    override fun addFavourite(holder: SubCompletedAdapter.ViewHolder, position: Int) {
        holder.binding.star.visibility = View.VISIBLE
        holder.binding.starEmpty.visibility = View.INVISIBLE
        val subTasksEntity = subTasks.completedSubList[position]
        subTasksEntity.isFavourite = true
        tasksDatabase.daoInterface().updateSubTask(subTasksEntity)
        subTasks.getSubFavourites()
    }

    override fun showDialog(isChecked : Boolean, holder: SubCompletedAdapter.ViewHolder, position : Int) {
        if(!isChecked){
            AlertDialog.Builder(requireContext())
                .setTitle(" Confirmation !!")
                .setMessage("\n Mark as Uncompleted..\n")
                .setPositiveButton("Yes"){_,_ ->
                    itemAddRemove(position)
                    refreshSubCompleted()
                }
                .setNegativeButton("No"){_,_ ->
                    holder.binding.mark.isChecked = true
                }
                .setCancelable(false)
                .create()
                .show()
        }
    }

    override fun startDetails(entity: SubTaskEntity) {
        val gson = Gson()
        val json = gson.toJson(entity)
        val intent = Intent(requireContext(), DetailsSubTask::class.java)
        intent.putExtra("entityJson", json) // json is the converted JSON string
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifyAdapter() {
        subCompletedAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun itemAddRemove(position: Int) {
        val subTasksEntity = subTasks.completedSubList[position]
        subTasksEntity.isCompleted = false
        subTasksEntity.dateCompleted = null
        tasksDatabase.daoInterface().updateSubTasksCompleted(subTasks.id,false)
        tasksDatabase.daoInterface().updateSubTask(subTasksEntity)
        subTasks.getSubData()
        subTasks.getSubDataCompleted()
        subCompletedAdapter.notifyDataSetChanged()
        subTasks.getSubFavourites()
    }
}