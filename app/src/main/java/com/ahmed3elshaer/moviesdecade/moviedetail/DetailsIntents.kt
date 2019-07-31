package com.ahmed3elshaer.moviesdecade.moviedetail

import com.ahmed3elshaer.moviesdecade.mvibase.MviIntent

sealed class DetailsIntents : MviIntent {
    data class QueryIntent(val query: String) : DetailsIntents()
}