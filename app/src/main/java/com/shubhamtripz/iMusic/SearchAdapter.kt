package com.shubhamtripz.mintsongspodcast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shubhamtripz.iMusic.R

class SearchAdapter(private val searchResults: List<Popular_song_model>, private val itemClickListener: (Popular_song_model) -> Unit) :


    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.searchItemImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.searchItemTitleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = searchResults[position]

        Glide.with(holder.itemView.context)
            .load(result.Image)
            .placeholder(R.drawable.musicholder)
            .into(holder.imageView)

        holder.titleTextView.text = result.Title

        holder.itemView.setOnClickListener {
            itemClickListener.invoke(result)
        }
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }


}