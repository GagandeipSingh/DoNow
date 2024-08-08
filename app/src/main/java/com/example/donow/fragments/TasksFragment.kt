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
import com.example.donow.activities.MainActivity
import com.example.donow.activities.SubTasks
import com.example.donow.activities.TaskDetails
import com.example.donow.adapters.TasksAdapter
import com.example.donow.dataBase.TasksDatabase
import com.example.donow.dataBase.TasksEntity
import com.example.donow.interFaces.InteractionInterFace0
import com.example.donow.databinding.FragmentTasksBinding
import com.google.gson.Gson
import java.util.Calendar


class TasksFragment : Fragment(), InteractionInterFace0 {
    private lateinit var binding : FragmentTasksBinding
    private lateinit var mainActivity : MainActivity
    private lateinit var tasksDatabase: TasksDatabase
    private lateinit var tasksAdapter: TasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        mainActivity.interactionInterFace0 = this
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
        refreshTasks()
        tasksDatabase = TasksDatabase.getInstance(requireContext())
        tasksAdapter = TasksAdapter(mainActivity.tasksList,mainActivity.interactionInterFace0)
        binding.tasks.layoutManager =LinearLayoutManager(requireContext())
        binding.tasks.adapter = tasksAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        mainActivity.getData()
        tasksAdapter.notifyDataSetChanged()
        refreshTasks()
    }

    override fun addedLast() {
        tasksAdapter.notifyItemInserted(mainActivity.tasksList.size - 1)
        refreshTasks()
    }

    override fun refreshTasks() {
        if(mainActivity.tasksList.size == 0){
            binding.noTasks.visibility = View.VISIBLE
            binding.noTasksTxt.visibility = View.VISIBLE
        }
        else{
            binding.noTasks.visibility = View.GONE
            binding.noTasksTxt.visibility = View.GONE
        }
    }

    override fun showDialog(isChecked : Boolean, holder : TasksAdapter.ViewHolder, position : Int, taskId : Int) {
        if(isChecked){
            AlertDialog.Builder(requireContext())
                .setTitle(" Confirmation !!")
                .setMessage("\n Task is Completed..\n")
                .setPositiveButton("Yes"){_,_ ->
                    itemAddRemove1(position, taskId)
                    holder.binding.mark.isChecked = true
                    refreshTasks()
                }
                .setNegativeButton("No"){_,_ ->
                    holder.binding.mark.isChecked = false
                }
                .setCancelable(false)
                .create()
                .show()
        }
        if(!isChecked){
            AlertDialog.Builder(requireContext())
                .setTitle(" Confirmation !!")
                .setMessage("\n Mark as Uncompleted..\n")
                .setPositiveButton("Yes"){_,_ ->
                    itemAddRemove2(position, taskId)
                    holder.binding.mark.isChecked = false
                    refreshTasks()
                }
                .setNegativeButton("No"){_,_ ->
                    holder.binding.mark.isChecked = true
                }
                .setCancelable(false)
                .create()
                .show()
        }
    }

    override fun addFavourite(holder: TasksAdapter.ViewHolder, position: Int) {
        holder.binding.star.visibility = View.VISIBLE
        holder.binding.starEmpty.visibility = View.INVISIBLE
        val tasksEntity = mainActivity.tasksList[position]
        tasksEntity.isFavourite = true
        tasksDatabase.daoInterface().updateTask(tasksEntity)
        mainActivity.getFavourites()
    }

    override fun removeFavourite(holder: TasksAdapter.ViewHolder, position: Int) {
        holder.binding.star.visibility = View.INVISIBLE
        holder.binding.starEmpty.visibility = View.VISIBLE
        val tasksEntity = mainActivity.tasksList[position]
        tasksEntity.isFavourite = false
        tasksDatabase.daoInterface().updateTask(tasksEntity)
        mainActivity.getFavourites()
    }

    override fun startActivity(taskName : String, id : Int) {
        val intent = Intent(requireContext(), SubTasks::class.java)
        intent.putExtra("taskName", taskName)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun startDetails(entity: TasksEntity) {
        val gson = Gson()
        val json = gson.toJson(entity)
        val intent = Intent(requireContext(), TaskDetails::class.java)
        intent.putExtra("entityJson", json) // json is the converted JSON string
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifyAdapter() {
        tasksAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun itemAddRemove1(position: Int, taskId: Int) {
        val tasksEntity = mainActivity.tasksList[position]
        tasksEntity.isCompleted = true
        tasksEntity.dateCompleted = Calendar.getInstance().time
        tasksDatabase.daoInterface().updateTask(tasksEntity)
        tasksDatabase.daoInterface().updateSubTasksCompleted(taskId, true)
        tasksDatabase.daoInterface().subTasksCompleted(taskId,true)
        mainActivity.getData()
        tasksAdapter.notifyDataSetChanged()
        mainActivity.getDataCompleted()
        mainActivity.getFavourites()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun itemAddRemove2(position: Int, taskId: Int) {
        val tasksEntity = mainActivity.tasksList[position]
        tasksEntity.isCompleted = false
        tasksEntity.dateCompleted = null
        tasksDatabase.daoInterface().updateTask(tasksEntity)
        tasksDatabase.daoInterface().updateSubTasksCompleted(taskId, false)
        tasksDatabase.daoInterface().subTasksCompleted(taskId,false)
        mainActivity.getData()
        tasksAdapter.notifyDataSetChanged()
        mainActivity.getDataCompleted()
        mainActivity.getFavourites()
    }
}