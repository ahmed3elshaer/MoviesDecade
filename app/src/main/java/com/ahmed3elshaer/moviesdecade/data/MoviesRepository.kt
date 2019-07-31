package com.ahmed3elshaer.moviesdecade.data

import android.content.Context
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.network.FlickerApi
import javax.inject.Inject


class MoviesRepository @Inject constructor(
    var flickerApi: FlickerApi,
    var context: Context
) {

    fun getMovies(page: Int): List<Movie> {
        return MoviesProvider.getMovies(context, page)
    }


    fun searchMovies(query: String): List<Any> {
        return MoviesProvider.searchMovies(context, query)
    }

    fun loadMoreSearch(page: Int): List<Any> {
        return MoviesProvider.loadMoreSearch(page)
    }

    fun currentSearchPage():Int{
        return MoviesProvider.getCurrentSearchSize()
    }
    fun currentMoviesPage():Int{
        return MoviesProvider.getCurrentMoviesSize()
    }


}