package com.ernest.reed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView

class MomentAdapter(private val moments: List<Moment>) : RecyclerView.Adapter<MomentAdapter.MomentViewHolder>() {

    class MomentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.momentLabel)
        val image: ImageView = view.findViewById(R.id.momentImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_moment, parent, false)
        return MomentViewHolder(view)
    }

    override fun onBindViewHolder(holder: MomentViewHolder, position: Int) {
        val moment = moments[position]
        holder.label.text = moment.label
        if (moment.imageUri != null) {
            holder.image.setImageURI(moment.imageUri)
        }
    }

    override fun getItemCount() = moments.size
}
