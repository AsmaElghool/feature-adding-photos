package com.example.pickerimages

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageSwitcher
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val images: ArrayList<Uri?>, private val mainView: View) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var currentSelectedPosition: Int = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon)

    }

    fun onDeleteIconClick(position: Int) {
        if (position in 0 until images.size) {
            images.removeAt(position)

            if (images.isNotEmpty()) {
                currentSelectedPosition = if (position >= images.size) {
                    images.size - 1
                } else {
                    position
                }

                updateImageSwitcher()
            } else {
                // images finished, clear the imageSwitcher
                mainView.findViewById<ImageSwitcher>(R.id.imageSwitcher).setImageURI(null)
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageURI(images[position])
        holder.itemView.setOnClickListener {
            // update the imageSwitcher
            mainView.findViewById<ImageSwitcher>(R.id.imageSwitcher).setImageURI(images[position])
            holder.deleteIcon.setOnClickListener {
                onDeleteIconClick(holder.adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    private fun updateImageSwitcher() {
        if (currentSelectedPosition >= 0 && currentSelectedPosition < images.size) {
            mainView.findViewById<ImageSwitcher>(R.id.imageSwitcher)
                .setImageURI(images[currentSelectedPosition])
        }
    }
}