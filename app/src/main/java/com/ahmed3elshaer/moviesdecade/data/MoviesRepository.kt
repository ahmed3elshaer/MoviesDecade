package com.ahmed3elshaer.moviesdecade.data

import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.network.FlickerApi
import io.reactivex.Observable
import javax.inject.Inject

class MoviesRepository @Inject constructor(flickerApi: FlickerApi){

    fun getMovies() : Observable<List<Movie>> {

    }

}