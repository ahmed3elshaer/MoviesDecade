package com.ahmed3elshaer.moviesdecade.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Long,

    @Json(name = "cast")
    @ColumnInfo(name = "cast")
    val cast: List<String> = listOf(),

    @Json(name = "year")
    @ColumnInfo(name = "year")
    val year: Int = 0,

    @Json(name = "genres")
    @ColumnInfo(name = "genres")
    val genres: List<String> = listOf(),

    @Json(name = "rating")
    @ColumnInfo(name = "rating")
    val rating: Int = 0,

    @ColumnInfo(name = "title")
    @Json(name = "title")
    val title: String = ""
) : Serializable

data class MoviesResponse(
    @Json(name = "movies")
    val movies: MutableList<Movie>
)
