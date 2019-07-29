package com.ahmed3elshaer.moviesdecade.movies

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmed3elshaer.moviesdecade.R
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import kotlinx.android.synthetic.main.movie_item.view.*
import java.lang.StringBuilder
import java.util.*

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    private var data: List<Movie> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) =
        holder.bind(data[position])

    fun setData(data: List<Movie>) {
        this.data = data
        notifyDataSetChanged()
    }


    class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Movie) = with(itemView) {
            val titleSpan = SpannableString(item.title + "(" + item.year.toString() + ")")
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
            setOnClickListener {
                // TODO: Handle on click
            }
        }
    }
}