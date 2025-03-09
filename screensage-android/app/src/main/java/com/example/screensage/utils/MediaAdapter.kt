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

/**
 * Adapter for displaying a list of media items (movies, shows, etc.) in a RecyclerView.
 *
 * This adapter binds a list of Media objects to a RecyclerView, displaying each
 * media item's image, title, and average rating.
 *
 * @param mediaList A list of Media objects to be displayed.
 * @param onItemClick A callback function that is triggered when a media item is clicked, passing the media item's ID.
 */
class MediaAdapter(
    private var mediaList: List<Media>,
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    /**
     * ViewHolder for each media item in the RecyclerView.
     *
     * This class holds references to the UI components of each item
     * in the media list (image, title, and rating).
     */
    class MediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.mediaImage)
        val titleView: TextView = view.findViewById(R.id.mediaTitle)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
    }

    /**
     * Creates and returns a new [MediaViewHolder] instance for a media item.
     *
     * @param parent The parent view that the new item view will be added to.
     * @param viewType The view type of the item (not used in this case).
     * @return A new instance of [MediaViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_item, parent, false)
        return MediaViewHolder(view)
    }

    /**
     * Binds the data from a specific media item to the UI elements in the ViewHolder.
     *
     * @param holder The [MediaViewHolder] holding the views to display the media item.
     * @param position The position of the media item in the data set.
     */
    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val media = mediaList[position]
        holder.titleView.text = media.title ?: media.name ?: "No title"
        holder.ratingBar.rating = media.averageRating.toFloat()
        Glide.with(holder.imageView.context).load("https://image.tmdb.org/t/p/w500${media.backdropPath}").into(holder.imageView)

        holder.itemView.setOnClickListener {
            onItemClick(media.id)
        }
    }

    /**
     * Returns the total number of items in the media list.
     *
     * @return The size of the media list.
     */
    override fun getItemCount() = mediaList.size

    /**
     * Updates the list of media items and notifies the adapter to refresh the UI.
     *
     * @param newMedia A new list of media items to replace the old list.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateMedia(newMedia: List<Media>) {
        mediaList = newMedia
        this.notifyDataSetChanged()
    }
}
