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
import com.example.donow.adapters.SubTasksAdapter
import com.example.donow.dataBase.SubTaskEntity
import com.example.donow.dataBase.TasksDatabase
import com.example.donow.interFaces.SubTasksInterFace0
import com.example.donow.databinding.FragmentTasksBinding
import com.google.gson.Gson
import java.util.Calendar

class SubTasksFragment : Fragment(), SubTasksInterFace0 {
    private lateinit var subTasksAdapter: SubTasksAdapter
    private lateinit var binding: FragmentTasksBinding
    private lateinit var subTasks : SubTasks
    private lateinit var tasksDatabase: TasksDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subTasks = activity as SubTasks
        subTasks.subTasksInterFace0 = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTasksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshSubTasks()
        tasksDatabase = TasksDatabase.getInstance(requireContext())
        subTasksAdapter = SubTasksAdapter(subTasks.subTasksList, subTasks.subTasksInterFace0)
        binding.tasks.layoutManager = LinearLayoutManager(requireContext())
        binding.tasks.adapter = subTasksAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val incompleteSubtaskCount = tasksDatabase.daoInterface().getIncompleteSubtaskCount(subTasks.id)
        val isAllSubtasksCompleted = incompleteSubtaskCount == 0
        tasksDatabase.daoInterface().updateSubTasksCompleted(subTasks.id, isAllSubtasksCompleted)
        subTasks.getSubData()
        subTasksAdapter.notifyDataSetChanged()
        refreshSubTasks()
    }

    override fun refreshSubTasks() {
        if(subTasks.subTasksList.size == 0){
            binding.noTasks.visibility = View.VISIBLE
            binding.noTasksTxt.visibility = View.VISIBLE
        }
        else{
            binding.noTasks.visibility = View.GONE
            binding.noTasksTxt.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifyAdapter() {
        subTasksAdapter.notifyDataSetChanged()
    }

    override fun addFavourite(holder: SubTasksAdapter.ViewHolder, position: Int) {
        holder.binding.star.visibility = View.VISIBLE
        holder.binding.starEmpty.visibility = View.INVISIBLE
        val subTaskEntity = subTasks.subTasksList[position]
        subTaskEntity.isFavourite = true
        tasksDatabase.daoInterface().updateSubTask(subTaskEntity)
        subTasks.getSubFavourites()
    }

    override fun removeFavourite(holder: SubTasksAdapter.ViewHolder, position: Int) {
        holder.binding.star.visibility = View.INVISIBLE
        holder.binding.starEmpty.visibility = View.VISIBLE
        val subTasksEntity = subTasks.subTasksList[position]
        subTasksEntity.isFavourite = false
        tasksDatabase.daoInterface().updateSubTask(subTasksEntity)
        subTasks.getSubFavourites()
    }

    override fun showDialog(isChecked: Boolean, holder: SubTasksAdapter.ViewHolder, position: Int) {
        if(isChecked){
            AlertDialog.Builder(requireContext())
                .setTitle(" Confirmation !!")
                .setMessage("\n Task is Completed..\n")
                .setPositiveButton("Yes"){_,_ ->
                    itemAddRemove(position)
                    holder.binding.mark.isChecked = false
                    refreshSubTasks()
                }
                .setNegativeButton("No"){_,_ ->
                    holder.binding.mark.isChecked = false
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
    private fun itemAddRemove(position: Int){
        val subTasksEntity = subTasks.subTasksList[position]
        subTasksEntity.isCompleted = true
        subTasksEntity.dateCompleted = Calendar.getInstance().time
        tasksDatabase.daoInterface().updateSubTask(subTasksEntity)
        val incompleteSubtaskCount = tasksDatabase.daoInterface().getIncompleteSubtaskCount(subTasks.id)
        val isAllSubtasksCompleted = incompleteSubtaskCount == 0
        tasksDatabase.daoInterface().updateSubTasksCompleted(subTasks.id, isAllSubtasksCompleted)
        subTasks.getSubData()
        subTasksAdapter.notifyDataSetChanged()
        subTasks.getSubDataCompleted()
        subTasks.getSubFavourites()
    }
}