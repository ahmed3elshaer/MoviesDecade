package com.ahmed3elshaer.moviesdecade.movies

import android.content.Context
import com.ahmed3elshaer.moviesdecade.data.MoviesProvider
import com.ahmed3elshaer.moviesdecade.data.models.Movie

class FakeMoviesProvider(context: Context): MoviesProvider(context) {
    val movies = listOf(
        Movie(
            1,
            listOf("Cast 1", "Cast 2"),
            2009,
            listOf("Science Fiction", "Drama"),
            3,
            "Movie 1"
        ),
        Movie(2, listOf("Cast 1", "Cast 2"), 2009, listOf("Science Fiction"), 5, "Movie 2"),
        Movie(3, listOf("Cast 1", "Cast 2"), 2009, listOf("Drama"), 3, "Movie 3")
    )

    val moviesSearch = listOf<Any>(
        2010,
        Movie(
            1,
            listOf("Cast 1", "Cast 2"),
            2009,
            listOf("Science Fiction", "Drama"),
            3,
            "Movie 1"
        ),
        2009,
        Movie(2, listOf("Cast 1", "Cast 2"), 2009, listOf("Science Fiction"), 5, "Movie 2"),
        Movie(3, listOf("Cast 1", "Cast 2"), 2009, listOf("Drama"), 3, "Movie 3")
    )

    override fun getMoviesLocal(): List<Movie> {
        return movies
    }

    fun getMoviesLocal(context: Context): List<Movie> {
        return movies
    }


}