package com.example.pickerimages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pickerimages.databinding.FragmentTextInputEdittextBottomSheetBinding
class TextInputEdittextBottomSheet : Fragment() {
    lateinit var binding: FragmentTextInputEdittextBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTextInputEdittextBottomSheetBinding.inflate(inflater, container, false)

        return binding.root
    }
}