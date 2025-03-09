package com.example.screensage.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.screensage.R
import com.example.screensage.entities.Review

/**
 * Adapter for displaying a list of reviews in a RecyclerView.
 *
 * This adapter binds a list of Review objects to a RecyclerView, displaying
 * each review's username, rating, and content.
 *
 * @param reviews A list of Review objects to be displayed.
 */
class ReviewAdapter(private var reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    /**
     * ViewHolder for each review item in the RecyclerView.
     *
     * This class holds references to the UI components of each item
     * in the review list (username, rating bar, and content).
     */
    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.reviewUsername)
        val ratingBar: RatingBar = view.findViewById(R.id.reviewRatingBar)
        val content: TextView = view.findViewById(R.id.reviewContent)
    }

    /**
     * Creates and returns a new [ReviewViewHolder] instance for a review item.
     *
     * @param parent The parent view that the new item view will be added to.
     * @param viewType The view type of the item (not used in this case).
     * @return A new instance of [ReviewViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    /**
     * Binds the data from a specific review to the UI elements in the ViewHolder.
     *
     * @param holder The [ReviewViewHolder] holding the views to display the review.
     * @param position The position of the review in the data set.
     */
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.username.text = review.user?.username ?: "Unknown"
        holder.ratingBar.rating = (review.rating ?: 0.0).toFloat()
        holder.content.text = review.content ?: "No content"
    }

    /**
     * Returns the total number of items in the reviews list.
     *
     * @return The size of the reviews list.
     */
    override fun getItemCount(): Int = reviews.size

    /**
     * Updates the list of reviews and notifies the adapter to refresh the UI.
     *
     * @param newReviews A new list of reviews to replace the old list.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateReviews(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}
