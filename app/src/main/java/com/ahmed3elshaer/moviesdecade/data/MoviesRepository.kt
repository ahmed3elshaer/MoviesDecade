package com.ahmed3elshaer.moviesdecade.data

import android.content.Context
import androidx.paging.DataSource
import com.ahmed3elshaer.moviesdecade.data.models.Image
import com.ahmed3elshaer.moviesdecade.data.models.ImageResponse
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.data.room.MoviesDao
import com.ahmed3elshaer.moviesdecade.network.FlickerApi
import com.ahmed3elshaer.moviesdecade.utils.ioThread
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject


class MoviesRepository @Inject constructor(
    var moviesDao: MoviesDao,
    var flickerApi: FlickerApi,
    var context: Context
) {
    private var movies: List<Movie> = mutableListOf()

    init {
        ioThread {
            movies = moviesDao.allMovies()
            if (movies.isEmpty()) {
                MoviesProvider.getMoviesLocal(context).apply {
                    movies = this
                }
            }
        }
    }


    fun allMoviesDataSource(): DataSource.Factory<Int, Movie> =
        moviesDao.allMoviesDataSource()

    fun searchMovies(query: String): List<Any> {
        return MoviesProvider.searchMovies(query, movies)
    }

    fun loadMoreSearch(page: Int): List<Any> {
        return MoviesProvider.loadMoreSearch(page)
    }

    fun currentSearchPage(): Int {
        return MoviesProvider.getCurrentSearchSize()
    }

    fun queryImages(title: String): Observable<List<Image>> {
        return flickerApi.queryMovie(title = title).map { response ->
            response.imageResponse.images
        }
    }


}