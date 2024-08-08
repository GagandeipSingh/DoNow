package com.example.donow.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.donow.fragments.CompletedFragment
import com.example.donow.fragments.FavouritesFragment
import com.example.donow.fragments.TasksFragment

class PageAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->{
                TasksFragment()
            }
            1 ->{
                FavouritesFragment()
            }
            else ->{
                CompletedFragment()
            }
        }
    }
}