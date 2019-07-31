package com.ahmed3elshaer.moviesdecade.data.paging

import androidx.paging.DataSource
import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import javax.inject.Inject

class MoviesSearchDataSourceFactory @Inject constructor(
    var moviesRepository: MoviesRepository,
    var query:String
) :
    DataSource.Factory<Int, Any>() {
    override fun create(): DataSource<Int, Any> {
        return MoviesSearchDataSource(moviesRepository,query)
    }


}