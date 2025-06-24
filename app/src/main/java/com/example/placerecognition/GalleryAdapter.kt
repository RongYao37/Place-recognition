package com.example.placerecognition

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryAdapter(
    private var images: List<Uri>,
    private val onItemClick: (Uri) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.ImageViewHolder>() {

    private var selectedUri: Uri? = null

    fun setSelected(uri: Uri) {
        selectedUri = uri
        notifyDataSetChanged()
    }

    fun updateImages(newImages: List<Uri>) {
        images = newImages
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageThumbnail)
        val tick: ImageView = view.findViewById(R.id.checkmarkOverlay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_thumbnail, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = images[position]

        Glide.with(holder.itemView.context)
            .load(uri)
            .centerCrop()
            .into(holder.imageView)

        holder.tick.visibility = if (uri == selectedUri) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            onItemClick(uri)
        }
    }
}