package com.ahmed3elshaer.moviesdecade.data

import android.content.Context
import android.preference.PreferenceManager
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.data.models.MoviesResponse
import com.ahmed3elshaer.moviesdecade.utils.FIRST_TIME
import com.ahmed3elshaer.moviesdecade.utils.PAGE_COUNT
import com.ahmed3elshaer.moviesdecade.utils.PAGE_COUNT_MOVIES
import com.ahmed3elshaer.moviesdecade.utils.readAssetsFile
import com.squareup.moshi.Moshi
import java.util.*

open class MoviesProvider(val context: Context) {

    open fun getMoviesLocal(): List<Movie> {
        val moviesStr = context.assets.readAssetsFile("movies.json")
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(MoviesResponse::class.java)
        return jsonAdapter.fromJson(moviesStr).movies
    }
     fun setFirstTimeOpen() {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        sp.edit().putBoolean(FIRST_TIME, false).apply()
    }

     fun isFirstTimeOpen(): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getBoolean(FIRST_TIME, true)
    }


}