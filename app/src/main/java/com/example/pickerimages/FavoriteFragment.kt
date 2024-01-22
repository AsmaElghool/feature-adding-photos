package com.example.pickerimages

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.pickerimages.databinding.FragmentFavouriteBinding
import com.google.android.material.textfield.TextInputEditText

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)


        adjustUserPhoneIcons(binding.userPhoneNumber, R.drawable.ic_phone_number)
        adjustUserPhoneIcons(binding.userWhatsappNumber, R.drawable.ic_whatsapp)
        drawableStartClicked(binding.userPhoneNumber)
        drawableStartClicked(binding.userWhatsappNumber)
        return binding.root
    }

    fun adjustUserPhoneIcons(editText: EditText, clearIconResource: Int) {
        // Set the custom clear icon as drawableEnd initially
        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, clearIconResource, 0)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                // if the edit text is empty
                if (editable.isNullOrEmpty()) {
                    editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        clearIconResource,
                        0
                    )
                } else {
                    editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_clear_edittext_content,
                        0,
                        clearIconResource,
                        0
                    )
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    fun drawableStartClicked(editText: EditText) {
        editText.setOnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2
            val rightDrawable = editText.compoundDrawables[DRAWABLE_RIGHT]

            if (event.action == MotionEvent.ACTION_UP && rightDrawable != null) {
                val drawableWidth = rightDrawable.bounds.width()

                if (event.rawX >= (editText.right - drawableWidth)) {
                    editText.text?.clear()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }
}