package com.example.donow.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donow.activities.MainActivity
import com.example.donow.activities.SubTasks
import com.example.donow.activities.TaskDetails
import com.example.donow.adapters.CompletedAdapter
import com.example.donow.dataBase.TasksDatabase
import com.example.donow.dataBase.TasksEntity
import com.example.donow.interFaces.InteractionInterFace2
import com.example.donow.databinding.FragmentCompletedBinding
import com.google.gson.Gson

class CompletedFragment : Fragment(), InteractionInterFace2 {
    private lateinit var binding : FragmentCompletedBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var completedAdapter: CompletedAdapter
    private lateinit var tasksDatabase : TasksDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        mainActivity.interactionInterFace2 = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentCompletedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksDatabase = TasksDatabase.getInstance(requireContext())
        refreshCompleted()
        completedAdapter = CompletedAdapter(mainActivity.completedList,mainActivity.interactionInterFace2)
        binding.tasks.adapter = completedAdapter
        binding.tasks.layoutManager = LinearLayoutManager(requireContext())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        mainActivity.getDataCompleted()
        completedAdapter.notifyDataSetChanged()
        refreshCompleted()
    }

    override fun refreshCompleted() {
        if(mainActivity.completedList.size == 0){
            binding.noTasks.visibility = View.VISIBLE
            binding.completedTasks.visibility = View.VISIBLE
        }
        else{
            binding.noTasks.visibility = View.GONE
            binding.completedTasks.visibility = View.GONE
        }
    }

    override fun removeFavourite(holder: CompletedAdapter.ViewHolder, position: Int) {
        holder.binding.star.visibility = View.INVISIBLE
        holder.binding.starEmpty.visibility = View.VISIBLE
        val tasksEntity = mainActivity.completedList[position]
        tasksEntity.isFavourite = false
        tasksDatabase.daoInterface().updateTask(tasksEntity)
        mainActivity.getFavourites()
    }

    override fun addFavourite(holder: CompletedAdapter.ViewHolder, position: Int) {
        holder.binding.star.visibility = View.VISIBLE
        holder.binding.starEmpty.visibility = View.INVISIBLE
        val tasksEntity = mainActivity.completedList[position]
        tasksEntity.isFavourite = true
        tasksDatabase.daoInterface().updateTask(tasksEntity)
        mainActivity.getFavourites()
    }

    override fun showDialog(isChecked : Boolean, holder : CompletedAdapter.ViewHolder, position : Int, taskId : Int) {
        if(!isChecked){
            AlertDialog.Builder(requireContext())
                .setTitle(" Confirmation !!")
                .setMessage("\n Mark as Uncompleted..\n")
                .setPositiveButton("Yes"){_,_ ->
                    itemAddRemove(position, taskId)
                    refreshCompleted()
                }
                .setNegativeButton("No"){_,_ ->
                    holder.binding.mark.isChecked = true
                }
                .setCancelable(false)
                .create()
                .show()
        }
    }

    override fun startActivity(taskName: String, id: Int) {
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
        completedAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun itemAddRemove(position: Int, taskId: Int) {
        val tasksEntity = mainActivity.completedList[position]
        tasksEntity.isCompleted = false
        tasksEntity.dateCompleted = null
        tasksDatabase.daoInterface().updateTask(tasksEntity)
        tasksDatabase.daoInterface().updateSubTasksCompleted(taskId, false)
        tasksDatabase.daoInterface().subTasksCompleted(taskId,false)
        mainActivity.getData()
        mainActivity.getDataCompleted()
        completedAdapter.notifyDataSetChanged()
        mainActivity.getFavourites()
    }
}