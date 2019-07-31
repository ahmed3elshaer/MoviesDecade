package com.ahmed3elshaer.moviesdecade.moviedetail

import com.ahmed3elshaer.moviesdecade.mvibase.MviAction

sealed class DetailsActions : MviAction {
    data class QueryImages(val query: String) : DetailsActions()
}
