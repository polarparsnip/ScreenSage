package com.example.screensage.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.screensage.R
import com.example.screensage.network.UserScore

/**
 * Adapter for displaying a list of user scores (scoreboard) in a RecyclerView.
 *
 * This adapter binds a list of UserScore objects to a RecyclerView, displaying each
 * user's username and score.
 *
 * @param userScores A list of user score item objects to be displayed.
 * @param page The current page number for the scoreboard.
 */
class ScoreboardAdapter(
    private var userScores: List<UserScore>,
    private var page: Int
) : RecyclerView.Adapter<ScoreboardAdapter.ScoreboardViewHolder>() {

    /**
     * ViewHolder for each user score item in the RecyclerView.
     *
     * This class holds references to the UI components of each item
     * in the scoreboard.
     */
    inner class ScoreboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rankText: TextView = view.findViewById(R.id.textRank)
        val usernameText: TextView = view.findViewById(R.id.textUsername)
        val scoreText: TextView = view.findViewById(R.id.textScore)
    }

    /**
     * Creates and returns a new [ScoreboardViewHolder] instance for a media item.
     *
     * @param parent The parent view that the new item view will be added to.
     * @param viewType The view type of the item (not used in this case).
     * @return A new instance of [ScoreboardViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.scoreboard_item, parent, false)
        return ScoreboardViewHolder(view)
    }

    /**
     * Binds the data from a specific user score item to the UI elements in the ViewHolder.
     *
     * @param holder The [ScoreboardViewHolder] holding the views to display the user score item.
     * @param position The position of the user score item in the data set.
     */
    override fun onBindViewHolder(holder: ScoreboardViewHolder, position: Int) {
        val userScore = userScores[position]
        holder.rankText.text = (1 + ((page - 1) * 20) + position).toString()
        holder.usernameText.text = userScore.user.username
        holder.scoreText.text = userScore.totalPoints.toString()
    }

    /**
     * Returns the total number of items in the scoreboard page.
     *
     * @return The size of the scoreboard page.
     */
    override fun getItemCount(): Int = userScores.size

    /**
     * Updates the list of user score items and notifies the adapter to refresh the UI.
     *
     * @param newScores A new list of user score items to replace the old list.
     * @param page A new page number to replace the old page number
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateScores(newScores: List<UserScore>, page: Int = 1) {
        userScores = newScores
        this.page = page
        notifyDataSetChanged()
    }
}
