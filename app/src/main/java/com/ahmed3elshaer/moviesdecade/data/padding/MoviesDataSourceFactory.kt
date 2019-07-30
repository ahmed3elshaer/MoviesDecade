package com.ahmed3elshaer.moviesdecade.data.padding

import android.content.Context
import androidx.paging.PageKeyedDataSource
import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MoviesDataSourceFactory @Inject constructor(
    var moviesRepository: MoviesRepository
) :
    PageKeyedDataSource<Int, Movie>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        moviesRepository.getMovies(0)
        callback.onResult(moviesRepository.getMovies(params.requestedLoadSize), null, 1)

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }

}