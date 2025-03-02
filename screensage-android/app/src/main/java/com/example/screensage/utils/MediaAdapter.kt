package com.example.screensage.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.screensage.R
import com.example.screensage.entities.Media

class MediaAdapter(
    private var mediaList: List<Media>,
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    class MediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.mediaImage)
        val titleView: TextView = view.findViewById(R.id.mediaTitle)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_item, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val media = mediaList[position]
        holder.titleView.text = media.title ?: media.name ?: "No title"
        holder.ratingBar.rating = media.averageRating.toFloat()
        Glide.with(holder.imageView.context).load("https://image.tmdb.org/t/p/w500${media.backdropPath}").into(holder.imageView)

        holder.itemView.setOnClickListener {
            onItemClick(media.id)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMedia(newMedia: List<Media>) {
        mediaList = newMedia
        this.notifyDataSetChanged()
    }

    override fun getItemCount() = mediaList.size
}
