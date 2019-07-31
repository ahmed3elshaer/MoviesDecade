package com.ahmed3elshaer.moviesdecade.data.room

import androidx.paging.DataSource
import androidx.room.*
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
@TypeConverters(Converter::class)
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Movie>)

    @Query("SELECT * FROM movies WHERE id = :id")
    fun noteById(id: Long): Single<Movie>

    @Query("SELECT * FROM movies ORDER BY id ASC")
    fun allMoviesDataSource(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movies ORDER BY id ASC")
    fun allMovies(): List<Movie>
}