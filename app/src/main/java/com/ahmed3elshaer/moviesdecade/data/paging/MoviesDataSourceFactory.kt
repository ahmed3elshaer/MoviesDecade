package com.ahmed3elshaer.moviesdecade.data.paging

import androidx.paging.DataSource
import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import javax.inject.Inject

class MoviesDataSourceFactory @Inject constructor(
    var moviesRepository: MoviesRepository
) :
    DataSource.Factory<Int, Movie>() {
    override fun create(): DataSource<Int, Movie> {
        return MoviesDataSource(moviesRepository)
    }


}