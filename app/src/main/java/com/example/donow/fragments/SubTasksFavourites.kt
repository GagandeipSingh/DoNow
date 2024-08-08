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
import com.example.donow.adapters.SubFavouritesAdapter
import com.example.donow.dataBase.SubTaskEntity
import com.example.donow.dataBase.TasksDatabase
import com.example.donow.interFaces.SubTasksInterFace1
import com.example.donow.databinding.FragmentSubTasksFavouritesBinding
import com.google.gson.Gson
import java.util.Calendar

class SubTasksFavourites : Fragment(), SubTasksInterFace1 {
    private lateinit var subFavouritesAdapter: SubFavouritesAdapter
    private lateinit var subTasksFavouritesBinding : FragmentSubTasksFavouritesBinding
    private lateinit var subTasks: SubTasks
    private lateinit var tasksDatabase: TasksDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subTasks = activity as SubTasks
        subTasks.subTasksInterFace1 = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        subTasksFavouritesBinding = FragmentSubTasksFavouritesBinding.inflate(layoutInflater)
        return subTasksFavouritesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksDatabase = TasksDatabase.getInstance(requireContext())
        refreshSubFavourites()
        subFavouritesAdapter = SubFavouritesAdapter(subTasks.favouriteSubList, subTasks.subTasksInterFace1)
        subTasksFavouritesBinding.tasks.adapter = subFavouritesAdapter
        subTasksFavouritesBinding.tasks.layoutManager = LinearLayoutManager(requireContext())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val incompleteSubtaskCount = tasksDatabase.daoInterface().getIncompleteSubtaskCount(subTasks.id)
        val isAllSubtasksCompleted = incompleteSubtaskCount == 0
        tasksDatabase.daoInterface().updateSubTasksCompleted(subTasks.id, isAllSubtasksCompleted)
        subTasks.getSubFavourites()
        refreshSubFavourites()
        subFavouritesAdapter.notifyDataSetChanged()
    }

    override fun refreshSubFavourites() {
        if(subTasks.favouriteSubList.size == 0){
            subTasksFavouritesBinding.noTasks.visibility = View.VISIBLE
            subTasksFavouritesBinding.favouriteTasksTxt.visibility = View.VISIBLE
        }
        else{
            subTasksFavouritesBinding.noTasks.visibility = View.GONE
            subTasksFavouritesBinding.favouriteTasksTxt.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun removeSubFavourites(holder: SubFavouritesAdapter.ViewHolder, position: Int) {
        val subTasksEntity = subTasks.favouriteSubList[position]
        subTasksEntity.isFavourite = false
        tasksDatabase.daoInterface().updateSubTask(subTasksEntity)
        subTasks.getSubFavourites()
        subTasks.getSubDataCompleted()
        subTasks.getSubData()
        subFavouritesAdapter.notifyDataSetChanged()
        refreshSubFavourites()
    }
    override fun showDialog(isChecked: Boolean, holder: SubFavouritesAdapter.ViewHolder, position: Int) {
        if(isChecked){
            AlertDialog.Builder(requireContext())
                .setTitle(" Confirmation !!")
                .setMessage("\n Task is Completed..\n")
                .setPositiveButton("Yes"){_,_ ->
                    itemAddRemove1(position)
                    holder.binding.mark.isChecked = true
                    refreshSubFavourites()
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
                    itemAddRemove2(position)
                    holder.binding.mark.isChecked = false
                    refreshSubFavourites()
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
        subFavouritesAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun itemAddRemove1(position: Int) {
        val subTasksEntity = subTasks.favouriteSubList[position]
        subTasksEntity.isCompleted = true
        subTasksEntity.dateCompleted = Calendar.getInstance().time
        tasksDatabase.daoInterface().updateSubTask(subTasksEntity)
        val incompleteSubtaskCount = tasksDatabase.daoInterface().getIncompleteSubtaskCount(subTasks.id)
        val isAllSubtasksCompleted = incompleteSubtaskCount == 0
        tasksDatabase.daoInterface().updateSubTasksCompleted(subTasks.id, isAllSubtasksCompleted)
        subTasks.getSubFavourites()
        subFavouritesAdapter.notifyDataSetChanged()
        subTasks.getSubDataCompleted()
        subTasks.getSubData()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun itemAddRemove2(position: Int) {
        val subTasksEntity = subTasks.favouriteSubList[position]
        subTasksEntity.isCompleted = false
        subTasksEntity.dateCompleted = null
        tasksDatabase.daoInterface().updateSubTasksCompleted(subTasks.id,false)
        tasksDatabase.daoInterface().updateSubTask(subTasksEntity)
        subTasks.getSubFavourites()
        subFavouritesAdapter.notifyDataSetChanged()
        subTasks.getSubDataCompleted()
        subTasks.getSubData()
    }
}