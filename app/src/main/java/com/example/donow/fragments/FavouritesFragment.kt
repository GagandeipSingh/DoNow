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
import com.example.donow.adapters.FavouritesAdapter
import com.example.donow.dataBase.TasksDatabase
import com.example.donow.dataBase.TasksEntity
import com.example.donow.interFaces.InteractionInterFace1
import com.example.donow.databinding.FragmentFavouritesBinding
import com.google.gson.Gson
import java.util.Calendar

class FavouritesFragment : Fragment(), InteractionInterFace1 {
    private lateinit var favouritesAdapter : FavouritesAdapter
    private lateinit var favouritesBinding: FragmentFavouritesBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var tasksDatabase: TasksDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        mainActivity.interactionInterFace1 = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        favouritesBinding = FragmentFavouritesBinding.inflate(layoutInflater)
        return favouritesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksDatabase = TasksDatabase.getInstance(requireContext())
        refreshFavourites()
        favouritesAdapter = FavouritesAdapter(mainActivity.favouriteList,mainActivity.interactionInterFace1)
        favouritesBinding.tasks.adapter = favouritesAdapter
        favouritesBinding.tasks.layoutManager = LinearLayoutManager(requireContext())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        mainActivity.getFavourites()
        refreshFavourites()
        favouritesAdapter.notifyDataSetChanged()

    }

    override fun refreshFavourites() {
        if(mainActivity.favouriteList.size == 0){
            favouritesBinding.noTasks.visibility = View.VISIBLE
            favouritesBinding.favouriteTasksTxt.visibility = View.VISIBLE
        }
        else{
            favouritesBinding.noTasks.visibility = View.GONE
            favouritesBinding.favouriteTasksTxt.visibility = View.GONE
        }
    }

    override fun addedLast() {
        favouritesAdapter.notifyItemInserted(mainActivity.favouriteList.size - 1)
        refreshFavourites()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun removeFavourite(holder: FavouritesAdapter.ViewHolder, position: Int) {
        val tasksEntity = mainActivity.favouriteList[position]
        tasksEntity.isFavourite = false
        tasksDatabase.daoInterface().updateTask(tasksEntity)
        mainActivity.getFavourites()
        mainActivity.getDataCompleted()
        mainActivity.getData()
        favouritesAdapter.notifyDataSetChanged()
        refreshFavourites()
    }

    override fun showDialog(isChecked: Boolean, holder: FavouritesAdapter.ViewHolder, position: Int, taskId : Int) {
        if(isChecked){
            AlertDialog.Builder(requireContext())
                .setTitle(" Confirmation !!")
                .setMessage("\n Task is Completed..\n")
                .setPositiveButton("Yes"){_,_ ->
                    itemAddRemove1(position, taskId)
                    holder.binding.mark.isChecked = true
                    refreshFavourites()
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
                    refreshFavourites()
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
        favouritesAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun itemAddRemove1(position: Int, taskId: Int) {
        val tasksEntity = mainActivity.favouriteList[position]
        tasksEntity.isCompleted = true
        tasksEntity.dateCompleted = Calendar.getInstance().time
        tasksDatabase.daoInterface().updateTask(tasksEntity)
        tasksDatabase.daoInterface().updateSubTasksCompleted(taskId, true)
        tasksDatabase.daoInterface().subTasksCompleted(taskId,true)
        mainActivity.getFavourites()
        favouritesAdapter.notifyDataSetChanged()
        mainActivity.getDataCompleted()
        mainActivity.getData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun itemAddRemove2(position: Int, taskId: Int) {
        val tasksEntity = mainActivity.favouriteList[position]
        tasksEntity.isCompleted = false
        tasksEntity.dateCompleted = null
        tasksDatabase.daoInterface().updateTask(tasksEntity)
        tasksDatabase.daoInterface().updateSubTasksCompleted(taskId, false)
        tasksDatabase.daoInterface().subTasksCompleted(taskId,false)
        mainActivity.getFavourites()
        favouritesAdapter.notifyDataSetChanged()
        mainActivity.getDataCompleted()
        mainActivity.getData()
    }
}