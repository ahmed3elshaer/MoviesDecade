package com.ahmed3elshaer.moviesdecade.data.padding

import androidx.paging.PageKeyedDataSource
import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.data.models.Movie

class MoviesSearchDataSource(
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
        callback.onResult(moviesRepository.getMovies(params.key), params.key + 1)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        callback.onResult(moviesRepository.getMovies(params.key), params.key - 1)
    }

}