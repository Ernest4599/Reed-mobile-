package com.ernest.reed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatarImage: ImageView = view.findViewById(R.id.avatarImage)
        val username: TextView = view.findViewById(R.id.username)
        val timestamp: TextView = view.findViewById(R.id.timestamp)
        val postText: TextView = view.findViewById(R.id.postText)
        val postImage: ImageView = view.findViewById(R.id.postImage)
        val likeCount: TextView = view.findViewById(R.id.likeCount)
        val commentCount: TextView = view.findViewById(R.id.commentCount)
        val shareCount: TextView = view.findViewById(R.id.shareCount)
        val viewComments: TextView = view.findViewById(R.id.viewComments)
        val followButton: TextView = view.findViewById(R.id.followButton)
    }

    private fun formatCount(count: Int): String {
        return if (count >= 1000) {
            String.format("%.1fK", count / 1000.0)
        } else {
            count.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        val isOwn = post.username == AppData.currentUsername

        holder.username.text = if (isOwn) AppData.currentDisplayName else post.username
        holder.timestamp.text = post.timestamp
        holder.postText.text = post.text
        holder.likeCount.text = formatCount(post.likes)
        holder.commentCount.text = formatCount(post.comments)
        holder.shareCount.text = formatCount(post.shares)
        holder.followButton.visibility = if (isOwn) View.GONE else View.VISIBLE

        if (post.comments > 0) {
            holder.viewComments.text = "View all ${post.comments} comments"
            holder.viewComments.visibility = View.VISIBLE
        } else {
            holder.viewComments.visibility = View.GONE
        }

        if (post.imageUri != null) {
            holder.postImage.setImageURI(post.imageUri)
        } else {
            holder.postImage.setImageDrawable(null)
        }

        val params = holder.postImage.layoutParams as ConstraintLayout.LayoutParams
        params.dimensionRatio = when (post.ratio) {
            PostRatio.PORTRAIT_3_4 -> "3:4"
            PostRatio.BALANCED_16_9 -> "16:9"
        }
        holder.postImage.layoutParams = params
    }

    override fun getItemCount() = posts.size
}
