package com.ahmed3elshaer.moviesdecade.data

import android.content.Context
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.data.models.MoviesResponse
import com.ahmed3elshaer.moviesdecade.network.FlickerApi
import com.ahmed3elshaer.moviesdecade.utils.readAssetsFile
import com.squareup.moshi.Moshi
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject


class MoviesRepository @Inject constructor(var flickerApi: FlickerApi, var context: Context) {
    private var moviesList = listOf<Movie>()
    fun getMovies(): Observable<List<Movie>> {
        return if (moviesList.isNotEmpty()) {
            Observable.just(moviesList)
        } else {
            val moviesStr = context.assets.readAssetsFile("movies.json")
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(MoviesResponse::class.java)
            moviesList = jsonAdapter.fromJson(moviesStr).movies
            Observable.just(moviesList)
        }

    }

    fun searchMovies(query: String): Observable<List<Movie>> {
        if (query.isEmpty())
            return Observable.just(moviesList)
        return if (moviesList.isNotEmpty()) {
            Observable.just(moviesList.filter {
                it.title.toLowerCase(Locale.getDefault())
                    .contains(query.toLowerCase(Locale.getDefault()))
            })
        } else
            Observable.error(Throwable("Empty movies file"))

    }

}