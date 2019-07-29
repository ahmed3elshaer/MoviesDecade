package com.ahmed3elshaer.moviesdecade.movies

import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.mvibase.MviViewState

data class MoviesViewStates(
    val isLoading: Boolean,
    val movies: List<Movie>,
    val error: Throwable?
) : MviViewState {
    companion object {
        fun idle(): MoviesViewStates {
            return MoviesViewStates(
                isLoading = false,
                movies = listOf(),
                error = null
            )
        }
    }

}
