package com.ernest.reed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RaiAdapter(private val messages: List<RaiMessage>) : RecyclerView.Adapter<RaiAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bubble: TextView = view.findViewById(R.id.messageBubble)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rai_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bubble.text = message.text
        val params = holder.bubble.layoutParams as android.widget.LinearLayout.LayoutParams
        params.gravity = if (message.isUser) android.view.Gravity.END else android.view.Gravity.START
        holder.bubble.layoutParams = params
    }

    override fun getItemCount() = messages.size
}
