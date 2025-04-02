package com.example.screensage.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.screensage.R
import com.example.screensage.entities.MediaList

/**
 * Adapter for displaying a list of media items (movies, shows, etc.) in a RecyclerView.
 *
 * This adapter binds a list of Media objects to a RecyclerView, displaying each
 * media item's image, title, and average rating.
 *
 * @param mediaLists A list of MediaList objects to be displayed.
 * @param onItemClick A callback function that is triggered when a media item is clicked, passing the media item's ID.
 */
class MediaListsAdapter(
    private var mediaLists: List<MediaList>,
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<MediaListsAdapter.MediaListViewHolder>() {

    /**
     * ViewHolder for each media item in the RecyclerView.
     *
     * This class holds references to the UI components of each item
     * in the list of media lists (image and title).
     */
    class MediaListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.mediaListImage)
        val titleView: TextView = view.findViewById(R.id.mediaListTitle)
    }

    /**
     * Creates and returns a new [MediaListViewHolder] instance for a media list item.
     *
     * @param parent The parent view that the new item view will be added to.
     * @param viewType The view type of the item (not used in this case).
     * @return A new instance of [MediaListViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_lists_item, parent, false)
        return MediaListViewHolder(view)
    }

    /**
     * Binds the data from a specific media list item to the UI elements in the ViewHolder.
     *
     * @param holder The [MediaListViewHolder] holding the views to display the media list item.
     * @param position The position of the media list item in the data set.
     */
    override fun onBindViewHolder(holder: MediaListViewHolder, position: Int) {
        val mediaList = mediaLists[position]
        val image = if ((mediaList.mediaListItems?.size ?: 0) > 0) mediaList.mediaListItems?.get(0)?.mediaImg else ""
        holder.titleView.text = mediaList.title  ?: "No title"
        Glide.with(holder.imageView.context).load(image).into(holder.imageView)

        holder.itemView.setOnClickListener {
            onItemClick(mediaList.id)
        }
    }

    /**
     * Returns the total number of items in the list of media lists.
     *
     * @return The size of the list of media lists.
     */
    override fun getItemCount() = mediaLists.size

    /**
     * Updates the list of media lists and notifies the adapter to refresh the UI.
     *
     * @param newMediaLists A new list of media lists to replace the old list.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateMedia(newMediaLists: List<MediaList>) {
        mediaLists = newMediaLists
        this.notifyDataSetChanged()
    }
}
