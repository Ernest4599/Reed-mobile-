package com.ernest.reed

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReelAdapter(private val reels: List<Reel>) : RecyclerView.Adapter<ReelAdapter.ReelViewHolder>() {

    private var activeUri: Uri? = null

    class ReelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.reelThumbnail)
        val video: CropVideoView = view.findViewById(R.id.reelVideo)
        val playButton: View = view.findViewById(R.id.playButton)
        val username: TextView = view.findViewById(R.id.reelUsername)
        val timestamp: TextView = view.findViewById(R.id.reelTimestamp)
        val caption: TextView = view.findViewById(R.id.reelCaption)
        val seeMore: TextView = view.findViewById(R.id.reelSeeMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reel, parent, false)
        return ReelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReelViewHolder, position: Int) {
        val reel = reels[position]
        holder.username.text = reel.username
        holder.timestamp.text = reel.timestamp
        holder.caption.text = reel.caption
        holder.caption.maxLines = 2
        holder.caption.ellipsize = android.text.TextUtils.TruncateAt.END
        holder.seeMore.visibility = View.GONE

        holder.caption.post {
            if (holder.caption.lineCount > 2 || (holder.caption.layout?.getEllipsisCount(1) ?: 0) > 0) {
                holder.seeMore.visibility = View.VISIBLE
            }
        }

        var expanded = false
        val toggleCaption = View.OnClickListener {
            expanded = !expanded
            if (expanded) {
                holder.caption.maxLines = Int.MAX_VALUE
                holder.caption.ellipsize = null
                holder.seeMore.text = "See less"
            } else {
                holder.caption.maxLines = 2
                holder.caption.ellipsize = android.text.TextUtils.TruncateAt.END
                holder.seeMore.text = "See more"
            }
        }
        holder.seeMore.setOnClickListener(toggleCaption)

        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(holder.itemView.context, reel.videoUri)
            holder.thumbnail.setImageBitmap(retriever.frameAtTime)
            retriever.release()
        } catch (e: Exception) {
            holder.thumbnail.setImageDrawable(null)
        }

        val isActive = reel.videoUri == activeUri
        if (isActive) {
            // Keep thumbnail INVISIBLE (not GONE) so it still holds its size,
            // since other views are constrained relative to it.
            holder.thumbnail.visibility = View.INVISIBLE
            holder.video.visibility = View.VISIBLE
            holder.playButton.visibility = View.GONE
            holder.video.setVideoURI(reel.videoUri)
            holder.video.setOnPreparedListener { mp ->
                mp.isLooping = true
                holder.video.setVideoDimensions(mp.videoWidth, mp.videoHeight)
            }
            holder.video.start()

            val toggle = View.OnClickListener {
                if (holder.video.isPlaying) {
                    holder.video.pause()
                    holder.playButton.visibility = View.VISIBLE
                } else {
                    holder.video.start()
                    holder.playButton.visibility = View.GONE
                }
            }
            holder.video.setOnClickListener(toggle)
            holder.playButton.setOnClickListener(toggle)
        } else {
            holder.thumbnail.visibility = View.VISIBLE
            holder.video.visibility = View.GONE
            holder.playButton.visibility = View.VISIBLE
            val play = View.OnClickListener {
                activeUri = reel.videoUri
                notifyDataSetChanged()
            }
            holder.playButton.setOnClickListener(play)
            holder.thumbnail.setOnClickListener(play)
        }
    }

    override fun getItemCount() = reels.size

    fun setActive(uri: Uri) {
        activeUri = uri
        notifyDataSetChanged()
    }
}
