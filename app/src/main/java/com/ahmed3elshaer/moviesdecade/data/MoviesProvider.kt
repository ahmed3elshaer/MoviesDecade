package com.ahmed3elshaer.moviesdecade.data

import android.content.Context
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.data.models.MoviesResponse
import com.ahmed3elshaer.moviesdecade.utils.PAGE_COUNT
import com.ahmed3elshaer.moviesdecade.utils.PAGE_COUNT_MOVIES
import com.ahmed3elshaer.moviesdecade.utils.readAssetsFile
import com.squareup.moshi.Moshi
import java.util.*

object MoviesProvider {

    private var searchMoviesChucked: MutableList<List<Any>> = mutableListOf()

    fun getMoviesLocal(context: Context): List<Movie> {
        val moviesStr = context.assets.readAssetsFile("movies.json")
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(MoviesResponse::class.java)
        return jsonAdapter.fromJson(moviesStr).movies
    }


    fun searchMovies(query: String, movies:List<Movie>): List<Any> {
        val list = movies.filter {
            it.title.toLowerCase(Locale.getDefault()).contains(
                query.toLowerCase(Locale.getDefault())
            )
        }.asReversed()
            .groupBy {
                it.year
            }
            .flatMap {
                var list = it.value.sortedByDescending { movie ->
                    movie.rating
                }.toMutableList<Any>()
                if (list.size > 5)
                    list = list.subList(0, 5)
                list.add(0, it.key)
                list
            }
        searchMoviesChucked.clear()
        if (list.size >= PAGE_COUNT)
            searchMoviesChucked = list.chunked(list.size / PAGE_COUNT).toMutableList()
        else
            searchMoviesChucked.add(0, list)

        return searchMoviesChucked[0]


    }

    fun loadMoreSearch(page: Int): List<Any> {
        return if (page < searchMoviesChucked.size && page >= 0)
            searchMoviesChucked[page]
        else
            listOf()
    }

    fun getCurrentSearchSize(): Int {
        return searchMoviesChucked.size
    }


}