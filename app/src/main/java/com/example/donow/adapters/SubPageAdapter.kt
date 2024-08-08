package com.example.donow.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.donow.fragments.SubTasksCompleted
import com.example.donow.fragments.SubTasksFavourites
import com.example.donow.fragments.SubTasksFragment

class SubPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->{
                SubTasksFragment()
            }
            1 ->{
                SubTasksFavourites()
            }
            else ->{
                SubTasksCompleted()
            }
        }
    }
}