package com.shubhamtripz.iMusic

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shubhamtripz.mintsongspodcast.Popular_song_model


class Popular_song_Adapter(private val context: Context, private val songList: List<Popular_song_model>):


    RecyclerView.Adapter<Popular_song_Adapter.ViewHolder>(){
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView);
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView);
        val artistTextView: TextView =itemView.findViewById(R.id.artistTextView);

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
       val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val songs= songList[position]
         holder.titleTextView.text= songs.Title;
        holder.artistTextView.text=songs.Artist

        if(!songs.Image.isBlank()){
            Glide.with(context)
                .load(songs.Image)
                .placeholder(R.drawable.musicholder)
                .into(holder.imageView)
        }
        else{
            //Load a placeholder image if the url is null or blank
            holder.imageView.setImageResource(R.drawable.musicholder)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("title", songs.Title ?: "")
            intent.putExtra("artist", songs.Artist ?: "")
            intent.putExtra("imageUrl", songs.Image ?: "")
            intent.putExtra("musicUrl", songs.Link ?: "")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }

}