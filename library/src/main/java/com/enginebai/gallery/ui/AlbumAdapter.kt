package com.enginebai.gallery.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.enginebai.gallery.library.R
import com.enginebai.gallery.model.ALL_MEDIA_ALBUM_NAME
import com.enginebai.gallery.model.AlbumItem
import kotlinx.android.synthetic.main.item_album.view.*

class AlbumAdapter(private val clickListener: (AlbumItem) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val albumList = mutableListOf<AlbumItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AlbumViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_album,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = albumList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as AlbumViewHolder
        val album = albumList[position]
        holder.bind(album)
        holder.itemView.setOnClickListener {
            clickListener(album)
        }
    }

}

class AlbumViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(album: AlbumItem) {
        Glide.with(view)
            .load(album.coverImagePath)
            .into(view.imageView)
        view.textAlbumName.text =
            if (album.name == ALL_MEDIA_ALBUM_NAME) view.context.getString(R.string.camera_roll) else album.name
        view.textMediaCount.text = album.mediaList.size.toString()
    }
}
