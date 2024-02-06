package com.example.pickerimages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pickerimages.adapters.MyVPAdapter
import com.example.pickerimages.databinding.FragmentTabLayoutBinding
import com.google.android.material.tabs.TabLayoutMediator

class TabLayoutFragment : Fragment() {
    private lateinit var binding: FragmentTabLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentTabLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myViewPager.adapter=MyVPAdapter(requireActivity())
        TabLayoutMediator(binding.myTabLayout,binding.myViewPager){ tab,position ->
            when(position){
                0 -> tab.text = "الحرفيون"
                1 -> tab.text = "الخدمات"
            }
        }.attach()

    }

}