package com.example.screensage.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.screensage.R
import com.example.screensage.entities.MediaList
import com.example.screensage.network.ScreensageApi
import com.example.screensage.service.AuthManager
import com.example.screensage.service.User
import kotlinx.coroutines.CoroutineScope
import java.util.Locale
import kotlinx.coroutines.launch

/**
 * Adapter for displaying a list of media items (movies, shows, etc.) in a RecyclerView.
 *
 * This adapter binds a list of Media objects to a RecyclerView, displaying each
 * media item's image, title, and average rating.
 *
 * @param mediaLists A list of MediaList objects to be displayed.
 * @param user Optional current [User].
 * @param context The context in which the adapter is used.
 * @param coroutineScope A [CoroutineScope] for launching coroutines in the adapter.
 * @param onItemClick A callback function that is triggered when a media list item is clicked, passing the media list's ID.
 * @param onIconClick A callback function that is triggered when a media list item icon is clicked, passing the media list's ID.
 */
class MediaListsAdapter(
    private var mediaLists: List<MediaList>,
    private var user: User? = null,
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val onItemClick: (Int) -> Unit,
    private val onIconClick: (Int) -> Unit
) :
    RecyclerView.Adapter<MediaListsAdapter.MediaListViewHolder>() {

    /**
     * ViewHolder for each media item in the RecyclerView.
     *
     * This class holds references to the UI components of each item
     * in the list of media lists.
     */
    class MediaListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.mediaListImage)
        val titleView: TextView = view.findViewById(R.id.mediaListTitle)
        val subtitleView: TextView = view.findViewById(R.id.mediaListSubtitle)
        val deleteView: ImageView = view.findViewById(R.id.mediaListDelete)
        val likeButtonView: ImageView = view.findViewById(R.id.listLikeButton)
        val likeCountTextView: TextView = view.findViewById(R.id.listLikeCount)
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
        holder.subtitleView.text = "Items in list: ${mediaList.mediaListItems.size}"
        Glide.with(holder.imageView.context).load(image).into(holder.imageView)

        holder.deleteView.visibility = View.GONE
        if (user != null && user!!.id == mediaList.user?.id) {
            holder.deleteView.visibility = View.VISIBLE
        }

        holder.deleteView.setOnClickListener {
            onIconClick(mediaList.id)
        }

        holder.itemView.setOnClickListener {
            onItemClick(mediaList.id)
        }

        if (mediaList.watchlist) {
            holder.likeButtonView.visibility = View.GONE
            holder.likeCountTextView.visibility = View.GONE
        } else {
            val likeCount = mediaList.likeCount ?: 0
            val isLiked = mediaList.userHasLiked ?: false

            if (isLiked) {
                holder.likeButtonView.setImageResource(R.drawable.thumbs_up_filled)
            } else {
                holder.likeButtonView.setImageResource(R.drawable.thumbs_up_outline)
            }
            holder.likeCountTextView.text = String.format(Locale.getDefault(), "%d", likeCount)

            holder.likeButtonView.setOnClickListener {
                toggleLike(mediaList, holder)
            }
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
    fun updateMedia(newMediaLists: List<MediaList>? = null) {
        if (newMediaLists != null) {
            mediaLists = newMediaLists
        }
        this.notifyDataSetChanged()
    }

    /**
     * Toggles the like status of a media list.
     * Sends a request to the API and updates the UI accordingly.
     */
    private fun toggleLike(mediaList: MediaList, holder: MediaListViewHolder) {
        val token = AuthManager.getToken(context) ?: return

        coroutineScope.launch {
            try {
                val response = ScreensageApi.retrofitService.likeMediaList(
                    "Bearer $token",
                    mediaList.id
                )

                if (response.isSuccessful) {
                    val newLikedStatus = !(mediaList.userHasLiked ?: false)
                    mediaList.userHasLiked = newLikedStatus
                    mediaList.likeCount = (mediaList.likeCount ?: 0) + if (newLikedStatus) 1 else -1

                    holder.likeButtonView.setImageResource(
                        if (newLikedStatus) R.drawable.thumbs_up_filled else R.drawable.thumbs_up_outline
                    )
                    holder.likeCountTextView.text = mediaList.likeCount.toString()

                    val message = response.body()?.string()
                    ToastUtil.showToast(context, message ?: "Like status updated")
                } else {
                    val errorMessage = ErrorUtil.parseApiErrorMessage(response)
                    ToastUtil.showToast(context, errorMessage)
                }
            } catch (e: Exception) {
                ToastUtil.showToast(context, "Network error: ${e.message}")
            }
        }
    }

}
