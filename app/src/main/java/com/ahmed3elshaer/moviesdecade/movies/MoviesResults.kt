package com.ahmed3elshaer.moviesdecade.movies

import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.mvibase.MviResult

sealed class MoviesResults : MviResult {
    sealed class LoadMoviesResult : MoviesResults() {
        data class Success(val movies: List<Movie>) : LoadMoviesResult()
        data class Failure(val error: Throwable) : LoadMoviesResult()
        object InFlight : LoadMoviesResult()
    }
}
