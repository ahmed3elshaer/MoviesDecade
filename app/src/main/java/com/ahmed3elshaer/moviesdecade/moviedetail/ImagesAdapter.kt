package com.ahmed3elshaer.moviesdecade.moviedetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmed3elshaer.moviesdecade.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.image_item.view.*
import java.util.*

class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {

    private var data: List<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        return ImagesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.image_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun swapData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) = with(itemView) {
            Glide.with(context).load(item).into(ivMovie)
        }
    }
}