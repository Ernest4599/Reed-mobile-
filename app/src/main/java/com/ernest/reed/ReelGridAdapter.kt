package com.ernest.reed

import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ReelGridAdapter(private val reels: List<Reel>) : RecyclerView.Adapter<ReelGridAdapter.GridViewHolder>() {

    class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.gridThumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reel_grid, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val reel = reels[position]
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(holder.itemView.context, reel.videoUri)
            holder.thumbnail.setImageBitmap(retriever.frameAtTime)
            retriever.release()
        } catch (e: Exception) {
            holder.thumbnail.setImageDrawable(null)
        }
    }

    override fun getItemCount() = reels.size
}
