package com.ahmed3elshaer.moviesdecade.data.models

import com.squareup.moshi.Json

data class Movie(@Json(name = "cast")
                 val cast: List<String> = mutableListOf(),
                 @Json(name = "year")
                 val year: Int = 0,
                 @Json(name = "genres")
                 val genres: List<String> = mutableListOf(),
                 @Json(name = "rating")
                 val rating: Int = 0,
                 @Json(name = "title")
                 val title: String = "")

data class MoviesResponse ( @Json(name = "movies")
                            val movies:MutableList<Movie>)
