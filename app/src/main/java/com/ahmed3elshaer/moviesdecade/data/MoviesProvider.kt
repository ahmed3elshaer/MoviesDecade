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

    private var moviesChucked: List<List<Movie>> = listOf()
    private var searchMoviesChucked: MutableList<List<Any>> = mutableListOf()
    private var movies: List<Movie> = listOf()
    private fun getMoviesLocal(context: Context): List<Movie> {
        if (movies.isEmpty()) {
            val moviesStr = context.assets.readAssetsFile("movies.json")
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(MoviesResponse::class.java)
            movies = jsonAdapter.fromJson(moviesStr).movies
        }
        return movies
    }

    private fun getChunkedMovies(context: Context): List<List<Movie>> {
        getMoviesLocal(context)
        if (moviesChucked.isEmpty())
            moviesChucked = movies.chunked(
                movies.size / PAGE_COUNT_MOVIES
            )
        return moviesChucked
    }


    fun getMovies(context: Context, page: Int = 0): List<Movie> {
        return if (page < getChunkedMovies(
                context
            ).size && page >= 0
        )
            getChunkedMovies(context)[page]
        else listOf()
    }

    fun searchMovies(context: Context, query: String): List<Any> {
        getMoviesLocal(context)
        getChunkedMovies(context)
        if (query.isEmpty())
            return moviesChucked[0]

        val list = movies.filter {
            it.title.toLowerCase(Locale.getDefault())
                .contains(query.toLowerCase(Locale.getDefault()))
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


}