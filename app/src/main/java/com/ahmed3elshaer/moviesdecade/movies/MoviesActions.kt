package com.ahmed3elshaer.moviesdecade.movies

import com.ahmed3elshaer.moviesdecade.mvibase.MviAction

sealed class MoviesActions : MviAction {
    object LoadMovies : MoviesActions()
    data class SearchMovies(val query: String) : MoviesActions()
}
