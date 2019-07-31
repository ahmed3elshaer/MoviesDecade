package com.ahmed3elshaer.moviesdecade.moviedetail

import androidx.paging.PagedList
import com.ahmed3elshaer.moviesdecade.mvibase.MviResult

sealed class DetailsResults : MviResult {

    sealed class QueryResult : DetailsResults() {
        data class Success(val urls: List<String>) : QueryResult()
        data class Failure(val error: Throwable) : QueryResult()
        object InFlight : QueryResult()
    }


}
