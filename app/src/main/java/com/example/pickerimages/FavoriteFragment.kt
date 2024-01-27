package com.example.pickerimages

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.pickerimages.databinding.FragmentFavouriteBinding
import com.example.pickerimages.databinding.FragmentTextInputEdittextBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding
    private var isTextExpanded = false
    private val maxCollapsedChars = 80
    private lateinit var bottomSheetBinding: FragmentTextInputEdittextBottomSheetBinding
    private lateinit var dialog: BottomSheetDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)

        adjustUserPhoneIcons(binding.userPhoneNumber, R.drawable.ic_phone_number)
        adjustUserPhoneIcons(binding.userWhatsappNumber, R.drawable.ic_whatsapp)
        drawableStartClicked(binding.userPhoneNumber)
        drawableStartClicked(binding.userWhatsappNumber)

        expandCollapseTextView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.showBottomSheetBtn.setOnClickListener {
            showTextInputEditTextDialog()
            dialog.show()
        }
        contactCardView()
    }


    private fun contactCardView() {
        binding.contactByWhattsappNumber.setOnClickListener {
            contactByWhattsapp()
        }
        binding.callByPhoneNumber.setOnClickListener {
            callByPhoneNumber()
        }
    }

    private fun callByPhoneNumber() {
        binding.callByPhoneNumber.setOnClickListener {
            makePhoneCall()
        }
    }

    private fun makePhoneCall() {
        if (PhonePermission.checkCallPhonePermission(requireActivity())) {
            // permission is granted
            val phoneNumber = binding.enterPhoneNumberEt.text.toString().trim()
            if (phoneNumber.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$phoneNumber")
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Please Enter Number ", Toast.LENGTH_LONG).show()
            }
        } else {
            PhonePermission.requestCallPhonePermission(requireActivity())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PhonePermission.CALL_PHONE_PERMISSION_CODE) {
            // handle phone call permission result
            PhonePermission.onRequestPermissionsResult(
                requireActivity(),
                requestCode,
                grantResults,
                ::makePhoneCall
            )
        }
    }


    fun contactByWhattsapp(){
        //        val phoneNumber = "01022287923"
        val phoneNumber = binding.enterPhoneNumberEt.text.toString().trim()
        val egyptCountryCode = "+20"
        if (phoneNumber.isNotEmpty()) {
            val fullPhoneNumber = "$egyptCountryCode$phoneNumber"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$fullPhoneNumber")
            startActivity(intent)
        }else{
            Toast.makeText(requireContext(), "Please Enter Number ", Toast.LENGTH_LONG).show()
        }
    }
    private fun showTextInputEditTextDialog() {
        dialog = BottomSheetDialog(requireContext())
        bottomSheetBinding = FragmentTextInputEdittextBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)
    }

    private fun expandCollapseTextView() {
        val fullText = getString(R.string.full_text)
        val spannableStringBuilder = SpannableStringBuilder()

        val displayText =
            if (isTextExpanded) fullText else getCollapsedText(fullText, maxCollapsedChars)
        spannableStringBuilder.append(displayText)

        val showMoreLessText =
            if (isTextExpanded) getString(R.string.show_less) else getString(R.string.show_more)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                toggleTextVisibility()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(requireContext(), R.color.main_color)
                ds.isUnderlineText = false
            }
        }
        spannableStringBuilder.append(" ")
        spannableStringBuilder.append(showMoreLessText, clickableSpan, 0)

        binding.longTextTextview.text = spannableStringBuilder
        binding.longTextTextview.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun toggleTextVisibility() {
        isTextExpanded = !isTextExpanded
        expandCollapseTextView()
    }

    private fun getCollapsedText(fullText: String, maxCollapsedChars: Int): CharSequence {
        return if (fullText.length > maxCollapsedChars) {
            "${fullText.substring(0, maxCollapsedChars)}..."
        } else {
            fullText
        }
    }

    private fun adjustUserPhoneIcons(editText: EditText, clearIconResource: Int) {
        // Set drawableEnd initially
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