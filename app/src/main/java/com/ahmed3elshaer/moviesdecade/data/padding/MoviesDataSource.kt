package com.ahmed3elshaer.moviesdecade.data.padding

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.data.models.Movie

class MoviesDataSource(
    var moviesRepository: MoviesRepository
) :
    PageKeyedDataSource<Int, Movie>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        callback.onResult(moviesRepository.getMovies(0), null, 1)

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        val adjacentKey = if (moviesRepository.currentMoviesPage() <= params.key)
            null
        else
            params.key + 1
        callback.onResult(moviesRepository.getMovies(params.key), adjacentKey)
        Log.d("Pagination", "load more ${params.key}")

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        callback.onResult(moviesRepository.getMovies(params.key), null)
        Log.d("Pagination", "load before ${params.key}")

    }

}