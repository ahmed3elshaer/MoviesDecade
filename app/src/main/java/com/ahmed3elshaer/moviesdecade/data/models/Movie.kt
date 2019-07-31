package com.ahmed3elshaer.moviesdecade.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Long,

    @Json(name = "cast")
    @ColumnInfo(name = "cast")
    val cast: List<String> = mutableListOf(),

    @Json(name = "year")
    @ColumnInfo(name = "year")
    val year: Int = 0,

    @Json(name = "genres")
    @ColumnInfo(name = "genres")
    val genres: List<String> = mutableListOf(),

    @Json(name = "rating")
    @ColumnInfo(name = "rating")
    val rating: Int = 0,

    @ColumnInfo(name = "title")
    @Json(name = "title")
    val title: String = ""
)

data class MoviesResponse(
    @Json(name = "movies")
    val movies: MutableList<Movie>
)
