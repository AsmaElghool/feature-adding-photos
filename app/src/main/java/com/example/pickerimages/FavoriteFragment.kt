package com.example.pickerimages

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.pickerimages.databinding.FragmentFavouriteBinding

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)

        drawableStartClicked()

        return binding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    fun drawableStartClicked() {
        binding.userPhoneNumber.setOnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.userPhoneNumber.right - binding.userPhoneNumber
                        .compoundDrawables[DRAWABLE_RIGHT].bounds.width())
                ) {
                    // clear the text when the drawableStart is clicked
                    binding.userPhoneNumber.text?.clear()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }
}