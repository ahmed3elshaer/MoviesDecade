package com.ahmed3elshaer.moviesdecade.moviedetail

import com.ahmed3elshaer.moviesdecade.mvibase.MviViewState

data class DetailsViewStates(
    val isLoading: Boolean,
    val urls: List<String>,
    val error: Throwable?
) : MviViewState {
    companion object {
        fun idle(): DetailsViewStates {
            return DetailsViewStates(
                isLoading = false,
                error = null,
                urls = listOf()
            )
        }
    }

}
