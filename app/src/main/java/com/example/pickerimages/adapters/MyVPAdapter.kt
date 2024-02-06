package com.example.pickerimages.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pickerimages.ArtisansFragment
import com.example.pickerimages.ServicesFragment

class MyVPAdapter(fa:FragmentActivity):FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0 -> ArtisansFragment()
            1 -> ServicesFragment()
            else -> ArtisansFragment()
        }
    }
}