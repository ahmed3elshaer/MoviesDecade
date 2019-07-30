package com.ahmed3elshaer.moviesdecade.movies

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmed3elshaer.moviesdecade.R
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.utils.TYPE_MOVIE
import com.ahmed3elshaer.moviesdecade.utils.TYPE_YEAR
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.android.synthetic.main.year_item.view.*


class MoviesSearchAdapter : PagedListAdapter<Any, RecyclerView.ViewHolder>(MoviesDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MOVIE -> MoviesViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.movie_item, parent, false)
            )
            TYPE_YEAR -> YearViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.year_item, parent, false)
            )
            else -> MoviesViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.movie_item, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position) is Int) {
            return TYPE_YEAR
        } else if (getItem(position) is Movie) {
            return TYPE_MOVIE
        }
        return -1
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_MOVIE -> (holder as MoviesViewHolder).bind(getItem(position) as Movie)
            TYPE_YEAR -> (holder as YearViewHolder).bind(getItem(position) as Int)
        }

    }



    class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Movie) = with(itemView) {
            val titleSpan = SpannableString(item.title + " (" + item.year.toString() + ")")
            titleSpan.setSpan(
                ForegroundColorSpan(Color.parseColor("#ABABAB")),
                item.title.length,
                titleSpan.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tvTitle.text = titleSpan
            val castStr = StringBuilder()
            item.cast.forEach { cast ->
                castStr.append(cast)
                if (cast != item.cast.last())
                    castStr.append(", ")

            }
            tvCast.text = castStr.toString()
            ratingBar.rating = item.rating.toFloat()
            setOnClickListener {

            }
        }
    }

    class YearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Int) = with(itemView) {
            tvYear.text = item.toString()
        }
    }

    companion object {
        val MoviesDiffCallback = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return if (oldItem is Movie && newItem is Movie) {
                    oldItem.title == newItem.title
                } else if (oldItem is Int && newItem is Int)
                    oldItem == newItem
                else
                    false
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return if (oldItem is Movie && newItem is Movie) {
                    oldItem as Movie == newItem as Movie
                } else if (oldItem is Int && newItem is Int)
                    oldItem as Int == newItem as Int
                else
                    false
            }
        }
    }
}