package com.example.donow.activities

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.donow.R
import com.example.donow.adapters.SubPageAdapter
import com.example.donow.dataBase.SubTaskEntity
import com.example.donow.dataBase.TasksDatabase
import com.example.donow.interFaces.SubTasksInterFace0
import com.example.donow.interFaces.SubTasksInterFace1
import com.example.donow.interFaces.SubTasksInterFace2
import com.example.donow.databinding.ActivitySubTasksBinding
import com.example.donow.databinding.CusDialogBinding
import com.example.donow.databinding.NavigateLayoutBinding
import com.example.donow.databinding.SortBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubTasks : AppCompatActivity() {
    private lateinit var binding: ActivitySubTasksBinding
    private lateinit var addBinding: CusDialogBinding
    private lateinit var description: String
    private lateinit var tasksDatabase: TasksDatabase
    lateinit var subTasksInterFace0: SubTasksInterFace0
    lateinit var subTasksInterFace1: SubTasksInterFace1
    lateinit var subTasksInterFace2: SubTasksInterFace2
    private lateinit var pageAdapter: SubPageAdapter
    var id: Int = 1
    var subTasksList: ArrayList<SubTaskEntity> = arrayListOf()
    var favouriteSubList: ArrayList<SubTaskEntity> = arrayListOf()
    var completedSubList: ArrayList<SubTaskEntity> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySubTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pageAdapter = SubPageAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = pageAdapter
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab((binding.tabLayout.getTabAt(position)))
            }
        })
        tasksDatabase = TasksDatabase.getInstance(this)
        binding.taskName.text = intent.getStringExtra("taskName")
        id = intent.getIntExtra("id", 1)
        binding.addTask.setOnClickListener {
            when (binding.tabLayout.selectedTabPosition) {
                0 -> addTask(false)
                1 -> {
                    addTask(true)
                }

                2 -> {
                    // Navigate to the first fragment
                    binding.viewPager.currentItem = 0  // Set ViewPager to first position
                    binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))  // Select first tab
                    lifecycleScope.launch {
                        delay(350L)
                        withContext(Dispatchers.Main) {
                            addTask(false)
                        }
                    }
                }
            }
        }
        binding.options.setOnClickListener {
            val dialog = Dialog(this)
            val customBinding = NavigateLayoutBinding.inflate(layoutInflater)
            dialog.setContentView(customBinding.root)
            customBinding.tasks.text = getString(R.string.sub_tasks)
            customBinding.tasks.setOnClickListener {
                binding.viewPager.currentItem = 0  // Set ViewPager to first position
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))  // Select first tab
                dialog.dismiss()
            }
            customBinding.favorites.setOnClickListener {
                binding.viewPager.currentItem = 1
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
                dialog.dismiss()
            }
            customBinding.completed.setOnClickListener {
                binding.viewPager.currentItem = 2
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(2))
                dialog.dismiss()
            }
            // Set dialog window attributes for full width and bottom positioning
            val window = dialog.window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.attributes?.gravity = Gravity.BOTTOM
            // Set background drawable for customization
            window?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_bg))
            dialog.show()
        }
        binding.sort.setOnClickListener {
            val dialog = Dialog(this)
            val customBinding = SortBinding.inflate(layoutInflater)
            dialog.setContentView(customBinding.root)
            customBinding.dateAsc.setOnClickListener {
                when (binding.tabLayout.selectedTabPosition) {
                    0 -> {
                        if(subTasksList.size != 0){
                            subTasksList.clear()
                            subTasksList.addAll(tasksDatabase.daoInterface().getSubTaskEntitiesAsc(id))
                            subTasksInterFace0.notifyAdapter()
                        }
                    }

                    1 -> {
                        if(favouriteSubList.size != 0){
                            favouriteSubList.clear()
                            favouriteSubList.addAll(tasksDatabase.daoInterface().getSubTaskEntitiesAsc(id))
                            subTasksInterFace1.notifyAdapter()
                        }
                    }

                    else -> {
                        if(completedSubList.size != 0){
                            completedSubList.clear()
                            completedSubList.addAll(tasksDatabase.daoInterface().getSubTaskEntitiesAscC(id))
                            subTasksInterFace2.notifyAdapter()
                        }
                    }
                }
                customBinding.tickAsc.visibility = View.VISIBLE
                customBinding.tickDesc.visibility = View.GONE
            }
            customBinding.dateDesc.setOnClickListener {
                when (binding.tabLayout.selectedTabPosition) {
                    0 -> {
                        if(subTasksList.size != 0){
                            subTasksList.clear()
                            subTasksList.addAll(tasksDatabase.daoInterface().getSubTaskEntitiesDesc(id))
                            subTasksInterFace0.notifyAdapter()
                        }
                    }
                    1 -> {
                        if(favouriteSubList.size != 0){
                            favouriteSubList.clear()
                            favouriteSubList.addAll(tasksDatabase.daoInterface().getSubTaskEntitiesDesc(id))
                            subTasksInterFace1.notifyAdapter()
                        }
                    }
                    else -> {
                        if(completedSubList.size != 0){
                            completedSubList.clear()
                            completedSubList.addAll(tasksDatabase.daoInterface().getSubTaskEntitiesDescC(id))
                            subTasksInterFace2.notifyAdapter()
                        }
                    }
                }
                customBinding.tickAsc.visibility = View.GONE
                customBinding.tickDesc.visibility = View.VISIBLE
            }
            val window = dialog.window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.attributes?.gravity = Gravity.BOTTOM
            window?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_bg))
            dialog.show()
        }
    }

    fun getSubData() {
        subTasksList.clear()
        subTasksList.addAll(tasksDatabase.daoInterface().getSubList(id))
    }

    private fun addTask(isFavourite: Boolean) {
        val dialog = Dialog(this)
        addBinding = CusDialogBinding.inflate(layoutInflater)
        dialog.setContentView(addBinding.root)
        dialog.setCancelable(false)
        // Set dialog window attributes for full width and bottom positioning
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.attributes?.gravity = Gravity.BOTTOM
        // Set background drawable for customization
        window?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_bg))
        dialog.show()
        addBinding.positiveButton.setOnClickListener {
            description = if (addBinding.etDescription.text?.trim()?.isEmpty()!!) {
                "No Description"
            } else {
                addBinding.etDescription.text.toString()
            }
            if (addBinding.etName.text?.trim()?.isEmpty()!!) {
                addBinding.textInputLayout1.error = "Enter Task.."
            } else {
                val subTaskEntity = SubTaskEntity(
                    subTask = addBinding.etName.text?.trim().toString(),
                    description = description,
                    taskId = id,
                    isFavourite = isFavourite,
                    dateCompleted = null
                )
                tasksDatabase.daoInterface().insertSubTask(subTaskEntity)
                tasksDatabase.daoInterface().updateSubTasksCompleted(id, false)
                Toast.makeText(this@SubTasks, "New Item Added..", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                if(isFavourite){
                    getSubFavourites()
                    subTasksInterFace1.notifyAdapter()
                }
                getSubData()
                subTasksInterFace0.notifyAdapter()
                subTasksInterFace0.refreshSubTasks()
            }
        }
        addBinding.negativeButton.setOnClickListener {
            dialog.dismiss()
        }
        addBinding.etName.doOnTextChanged { _, _, _, _ ->
            addBinding.textInputLayout1.isErrorEnabled = false
        }
    }

    fun getSubFavourites() {
        favouriteSubList.clear()
        favouriteSubList.addAll(tasksDatabase.daoInterface().getSubListFavorites(id))
    }

    fun getSubDataCompleted() {
        completedSubList.clear()
        completedSubList.addAll(tasksDatabase.daoInterface().getSubListCompleted(id))
    }
}