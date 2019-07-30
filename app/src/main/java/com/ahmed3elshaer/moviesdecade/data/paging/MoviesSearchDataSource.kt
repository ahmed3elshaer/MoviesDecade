package com.ahmed3elshaer.moviesdecade.data.paging

import androidx.paging.PageKeyedDataSource
import com.ahmed3elshaer.moviesdecade.data.MoviesRepository

class MoviesSearchDataSource(
    var moviesRepository: MoviesRepository,
    var query: String
) :
    PageKeyedDataSource<Int, Any>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Any>
    ) {
        callback.onResult(moviesRepository.searchMovies(query), null, 1)

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Any>) {
        val adjacentKey = if (moviesRepository.currentSearchPage() <= params.key)
            null
        else
            params.key + 1
        callback.onResult(moviesRepository.loadMoreSearch(params.key), adjacentKey)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Any>) {
    }

}