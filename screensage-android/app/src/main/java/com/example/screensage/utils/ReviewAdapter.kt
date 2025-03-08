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

class ReviewAdapter(private var reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.reviewUsername)
        val ratingBar: RatingBar = view.findViewById(R.id.reviewRatingBar)
        val content: TextView = view.findViewById(R.id.reviewContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.username.text = review.user?.username ?: "Unknown"
        holder.ratingBar.rating = (review.rating ?: 0.0).toFloat()
        holder.content.text = review.content ?: "No content"
    }

    override fun getItemCount(): Int = reviews.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateReviews(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}
