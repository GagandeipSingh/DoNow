package com.example.donow.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.get
import com.example.donow.R
import com.example.donow.dataBase.TasksDatabase
import com.example.donow.dataBase.TasksEntity
import com.example.donow.databinding.ActivityTaskDetailsBinding
import com.example.donow.databinding.CusDialogBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskDetails : AppCompatActivity() {
    private lateinit var binding : ActivityTaskDetailsBinding
    private lateinit var tasksDatabase: TasksDatabase
    private lateinit var updateBinding : CusDialogBinding
    private lateinit var description : String
    private var taskId : Long = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tasksDatabase = TasksDatabase.getInstance(this@TaskDetails)
        val gson = Gson()
        val entityJson = intent.getStringExtra("entityJson") ?: ""
        val tasksEntity : TasksEntity = gson.fromJson(entityJson, TasksEntity::class.java)
        binding.tvTaskName.text = tasksEntity.task
        binding.tvDescription.text = tasksEntity.description
        taskId = tasksEntity.id
        val formatter = SimpleDateFormat("dd/MMM/yyyy hh:mm a", Locale.getDefault())
        val formattedDate1 = tasksEntity.dateCreated?.let { formatter.format(it) } ?: getString(R.string.empty)
        binding.tvDateTime2.text = formattedDate1
        val formattedDate2 = tasksEntity.dateCompleted?.let { formatter.format(it) } ?: getString(R.string.empty)
        binding.tvDateTime4.text = formattedDate2
        binding.subTasks.setOnClickListener {
            val intent = Intent(this@TaskDetails, SubTasks::class.java)
            intent.putExtra("taskName", tasksEntity.task)
            intent.putExtra("id", tasksEntity.id)
            startActivity(intent)
        }
        binding.back.setOnClickListener {
            finish()
        }
        binding.options.setOnClickListener {
            val options = PopupMenu(this,it, Gravity.END)
            options.inflate(R.menu.menu_options)
            val updateItem = options.menu[1]
            updateItem.setOnMenuItemClickListener {
                updateTask()
                return@setOnMenuItemClickListener true
            }
            val deleteItem = options.menu[0]
            deleteItem.setOnMenuItemClickListener {
                AlertDialog.Builder(this)
                    .setTitle(" Confirmation !!")
                    .setMessage("\n Do you want to delete..\n")
                    .setPositiveButton("Yes"){_,_ ->
                        tasksDatabase.daoInterface().deleteTaskAndSubtasks(taskId)
                        finish()
                        Toast.makeText(this,"SubTask Deleted..", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No"){_,_ ->
                    }
                    .setCancelable(false)
                    .create()
                    .show()
                return@setOnMenuItemClickListener true
            }
            options.show()
        }
    }

    private fun updateTask() {
        val dialog = Dialog(this)
        updateBinding = CusDialogBinding.inflate(layoutInflater)
        dialog.setContentView(updateBinding.root)
        updateBinding.title.text = getString(R.string.update_task)
        updateBinding.positiveButton.text = getString(R.string.update)
        updateBinding.etName.setText(binding.tvTaskName.text.toString())
        updateBinding.etDescription.setText(binding.tvDescription.text.toString())
        dialog.setCancelable(false)
        // Set dialog window attributes for full width and bottom positioning
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.attributes?.gravity = Gravity.BOTTOM
        // Set background drawable for customization
        window?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_bg))
        dialog.show()
        updateBinding.negativeButton.setOnClickListener {
            dialog.dismiss()
        }
        updateBinding.positiveButton.setOnClickListener {
            description = if (updateBinding.etDescription.text?.trim()?.isEmpty()!!) {
                "No Description"
            } else {
                updateBinding.etDescription.text.toString()
            }
            if (updateBinding.etName.text?.trim()?.isEmpty()!!) {
                updateBinding.textInputLayout1.error = "Enter Task.."
            } else {
                val taskEntity = tasksDatabase.daoInterface().getEntity(taskId)
                taskEntity.task = updateBinding.etName.text?.trim().toString()
                taskEntity.description = description
                taskEntity.dateCreated = Calendar.getInstance().time
                tasksDatabase.daoInterface().updateTask(taskEntity)
                dialog.dismiss()
                finish()
                Toast.makeText(this,"Task is Updated..",Toast.LENGTH_SHORT).show()
            }
        }
    }
}