package com.ahmed3elshaer.moviesdecade.movies

import androidx.paging.PagedList
import com.ahmed3elshaer.moviesdecade.mvibase.MviResult

sealed class MoviesResults : MviResult {
    sealed class LoadMoviesResult : MoviesResults() {
        data class Success(val movies: PagedList<Any>) : LoadMoviesResult()
        data class Failure(val error: Throwable) : LoadMoviesResult()
        object InFlight : LoadMoviesResult()
    }

    sealed class SearchMoviesResult : MoviesResults() {
        data class Success(val movies: PagedList<Any>) : SearchMoviesResult()
        data class Failure(val error: Throwable) : SearchMoviesResult()
        object InFlight : SearchMoviesResult()
    }


}
