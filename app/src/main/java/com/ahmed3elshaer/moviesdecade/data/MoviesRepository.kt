package com.ahmed3elshaer.moviesdecade.data

import android.content.Context
import androidx.paging.DataSource
import com.ahmed3elshaer.moviesdecade.data.models.Image
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.data.room.MoviesDao
import com.ahmed3elshaer.moviesdecade.network.FlickerApi
import com.ahmed3elshaer.moviesdecade.utils.PAGE_COUNT
import com.ahmed3elshaer.moviesdecade.utils.ioThread
import com.example.android.architecture.blueprints.todoapp.util.SingletonHolderTripleArg
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject
import android.R.id.edit
import android.preference.PreferenceManager
import android.content.SharedPreferences
import com.ahmed3elshaer.moviesdecade.utils.FIRST_TIME
import com.ahmed3elshaer.moviesdecade.utils.ListDataSource
import org.junit.runner.RunWith

class MoviesRepository @Inject constructor(
    private val moviesProvider: MoviesProvider,
    private val moviesDao: MoviesDao,
    private val flickerApi: FlickerApi
) {
    private var movies: List<Movie> = mutableListOf()
    private var searchMoviesChucked: MutableList<List<Any>> = mutableListOf()


    fun allMoviesDataSource(): DataSource.Factory<Int, Movie> {
        if (moviesProvider.isFirstTimeOpen()) {
            moviesProvider.getMoviesLocal().apply {
                movies = this
                ioThread {
                    moviesDao.insertAll(this)
                }
                moviesProvider.setFirstTimeOpen()
                return ListDataSource(this)
            }
        } else {
            ioThread {
                if (movies.isEmpty())
                    movies = moviesDao.allMovies()
            }
            return moviesDao.allMoviesDataSource()
        }


    }


    fun searchMovies(query: String): List<Any> {
        val searchResults = movies
            //using filter to query titles
            .filter {
                it.title.toLowerCase(Locale.getDefault()).contains(
                    query.toLowerCase(Locale.getDefault())
                )
                //re gonna reverse it as the years should be
                // sorted from higher to lower year, the default from data
                //is lower to higher
            }.asReversed()
            //then we gonna group the results by years as a category
            .groupBy {
                it.year
            }
            //we gonna flatten the result map to transform
            // it to a List for RecyclerView adapter
            .flatMap {
                // we gonna sort the rating of an entity and
                // limit the results to get the top 5
                var list = it.value.sortedByDescending { movie ->
                    movie.rating
                }.toMutableList<Any>()
                if (list.size > 5)
                    list = list.subList(0, 5)
                // append the year value
                list.add(0, it.key)
                list
            }
        //clear the last search results and append the new ones
        searchMoviesChucked.clear()
        // chunking the search results for pagination and cache it
        if (searchResults.size >= PAGE_COUNT)
            searchMoviesChucked =
                searchResults.chunked(searchResults.size / PAGE_COUNT).toMutableList()
        else
            searchMoviesChucked.add(0, searchResults)

        return searchMoviesChucked[0]

    }

    fun loadMoreSearch(page: Int): List<Any> {
        // sending the next page of the search results for pagination
        return if (page < searchMoviesChucked.size && page >= 0)
            searchMoviesChucked[page]
        else
            listOf()
    }

    fun currentSearchPage(): Int {
        return searchMoviesChucked.size

    }

    fun queryImages(title: String): Observable<List<Image>> {
        return flickerApi.queryMovie(title = title).map { response ->
            response.imageResponse.images
        }
    }


}