package com.ahmed3elshaer.moviesdecade.data.padding

import android.content.Context
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
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