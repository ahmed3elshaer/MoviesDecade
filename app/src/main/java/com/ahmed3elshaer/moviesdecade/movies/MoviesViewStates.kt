package com.ahmed3elshaer.moviesdecade.movies

import androidx.paging.PagedList
import com.ahmed3elshaer.moviesdecade.mvibase.MviViewState

data class MoviesViewStates(
    val isLoading: Boolean,
    val movies: PagedList<Any>?,
    val moviesSearch: PagedList<Any>?,
    val error: Throwable?
) : MviViewState {
    companion object {
        fun idle(): MoviesViewStates {
            return MoviesViewStates(
                isLoading = false,
                error = null,
                movies = null,
                moviesSearch = null

            )
        }
    }

}
