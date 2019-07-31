package com.ahmed3elshaer.moviesdecade.movies

import com.ahmed3elshaer.moviesdecade.mvibase.MviIntent

sealed class MoviesIntents : MviIntent {
    object InitIntent : MoviesIntents()
    data class SearchIntent(val query: String) : MoviesIntents()
}