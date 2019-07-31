package com.ahmed3elshaer.moviesdecade.data.room

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ahmed3elshaer.moviesdecade.data.MoviesProvider
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.utils.ioThread

@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

    companion object {
        fun getInstance(context: Context): MoviesDatabase =
            Room.databaseBuilder(context, MoviesDatabase::class.java, "moviesdb")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        fillInDb(context)
                    }
                }).build()

        /**
         * fill database with list of notes so we can test pagination
         */
        private fun fillInDb(context: Context) {
            // inserts in Room are executed on the current thread, so we insert in the background
            ioThread {
                MoviesDatabase.getInstance(context).moviesDao().insertAll(
                    MoviesProvider.getMoviesLocal(context)
                )
            }
        }
    }
}
