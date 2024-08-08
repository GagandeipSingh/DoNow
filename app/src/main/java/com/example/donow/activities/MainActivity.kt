package com.example.donow.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.donow.adapters.PageAdapter
import com.example.donow.R
import com.example.donow.dataBase.TasksDatabase
import com.example.donow.dataBase.TasksEntity
import com.example.donow.interFaces.FragmentAct1
import com.example.donow.interFaces.InteractionInterFace0
import com.example.donow.interFaces.InteractionInterFace1
import com.example.donow.interFaces.InteractionInterFace2
import com.example.donow.databinding.ActivityMainBinding
import com.example.donow.databinding.CusDialogBinding
import com.example.donow.databinding.NavigateLayoutBinding
import com.example.donow.databinding.SortBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), FragmentAct1 {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PageAdapter
    private lateinit var addBinding: CusDialogBinding
    private lateinit var tasksDatabase: TasksDatabase
    lateinit var interactionInterFace0: InteractionInterFace0
    lateinit var interactionInterFace1: InteractionInterFace1
    lateinit var interactionInterFace2: InteractionInterFace2
    private lateinit var description :String
    var tasksList: ArrayList<TasksEntity> = arrayListOf()
    var completedList: ArrayList<TasksEntity> = arrayListOf()
    var favouriteList: ArrayList<TasksEntity> = arrayListOf()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0,  // Set left padding
                0,   // Set top padding
                0, // Set right padding
                systemBars.bottom  // Set bottom padding
            )
            insets
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        tasksDatabase = TasksDatabase.getInstance(this)
        adapter = PageAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewPager.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
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
                        if(tasksList.size != 0){
                            tasksList.clear()
                            tasksList.addAll(tasksDatabase.daoInterface().getTaskEntitiesAsc())
                            interactionInterFace0.notifyAdapter()
                        }
                    }
                    1 -> {
                        if(favouriteList.size != 0){
                            favouriteList.clear()
                            favouriteList.addAll(tasksDatabase.daoInterface().getTaskEntitiesAsc())
                            interactionInterFace1.notifyAdapter()
                        }
                    }
                    else -> {
                        if(completedList.size != 0){
                            completedList.clear()
                            completedList.addAll(tasksDatabase.daoInterface().getTaskEntitiesAscC())
                            interactionInterFace2.notifyAdapter()
                        }
                    }
                }
                customBinding.tickAsc.visibility = View.VISIBLE
                customBinding.tickDesc.visibility = View.GONE
            }
            customBinding.dateDesc.setOnClickListener {
                when (binding.tabLayout.selectedTabPosition) {
                    0 -> {
                        if(tasksList.size != 0){
                            tasksList.clear()
                            tasksList.addAll(tasksDatabase.daoInterface().getTaskEntitiesDesc())
                            interactionInterFace0.notifyAdapter()
                        }
                    }
                    1 -> {
                        if(favouriteList.size != 0){
                            favouriteList.clear()
                            favouriteList.addAll(tasksDatabase.daoInterface().getTaskEntitiesDesc())
                            interactionInterFace1.notifyAdapter()
                        }
                    }
                    else -> {
                        if(completedList.size != 0){
                            completedList.clear()
                            completedList.addAll(tasksDatabase.daoInterface().getTaskEntitiesDescC())
                            interactionInterFace2.notifyAdapter()
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
    override fun getData() {
        tasksList.clear()
        tasksList.addAll(tasksDatabase.daoInterface().getList())
    }
    override fun getDataCompleted() {
        completedList.clear()
        completedList.addAll(tasksDatabase.daoInterface().getListCompleted())
    }
    override fun getFavourites(){
        favouriteList.clear()
        favouriteList.addAll(tasksDatabase.daoInterface().getListFavorites())
    }
    private fun addTask(isFavourite : Boolean){
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
            description = if(addBinding.etDescription.text?.trim()?.isEmpty()!!){
                "No Description"
            } else{
                addBinding.etDescription.text.toString()
            }
            if (addBinding.etName.text?.trim()?.isEmpty()!!) {
                addBinding.textInputLayout1.error = "Enter Task.."
            }
                else {
                val taskEntity = TasksEntity(
                    task = addBinding.etName.text?.trim().toString(),
                    description = description,
                    isFavourite = isFavourite,
                    dateCompleted = null
                )
                tasksDatabase.daoInterface().insertTask(taskEntity)
                getData()
                interactionInterFace0.addedLast()
                if(isFavourite){
                    getFavourites()
                    interactionInterFace1.addedLast()
                }
                Toast.makeText(this@MainActivity, "New Item Added..", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        addBinding.negativeButton.setOnClickListener {
            dialog.dismiss()
        }
        addBinding.etName.doOnTextChanged { _, _, _, _ ->
            addBinding.textInputLayout1.isErrorEnabled = false
        }
    }
}