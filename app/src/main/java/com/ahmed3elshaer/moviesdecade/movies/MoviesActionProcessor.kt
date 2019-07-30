package com.ahmed3elshaer.moviesdecade.movies

import androidx.paging.PagedList
import androidx.paging.PagedList.Config.MAX_SIZE_UNBOUNDED
import androidx.paging.RxPagedListBuilder
import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.data.padding.MoviesDataSource
import com.ahmed3elshaer.moviesdecade.data.padding.MoviesDataSourceFactory
import com.ahmed3elshaer.moviesdecade.data.padding.MoviesSearchDataSourceFactory
import com.ahmed3elshaer.moviesdecade.utils.PAGE_COUNT
import com.ahmed3elshaer.moviesdecade.utils.PAGE_COUNT_MOVIES
import com.ahmed3elshaer.moviesdecade.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

@Suppress("UNCHECKED_CAST")
class MoviesActionProcessor(
    moviesRepo: MoviesRepository,
    moviesDataSourceFactory: MoviesDataSourceFactory,
    scheduler: BaseSchedulerProvider
) {

    var actionProcessor =
        ObservableTransformer<MoviesActions, MoviesResults> { actions ->
            actions.publish { shared ->
                Observable.merge(
                    shared.ofType(MoviesActions.LoadMovies::class.java).compose(loadMoviesProcessor),
                    shared.ofType(MoviesActions.SearchMovies::class.java).compose(
                        searchMoviesProcessor
                    )
                )
            }
        }

    private val loadMoviesProcessor =
        ObservableTransformer<MoviesActions.LoadMovies, MoviesResults> { action ->
            action.flatMap {
                val config = PagedList.Config.Builder()
                    .setPageSize(PAGE_COUNT_MOVIES)
                    .setInitialLoadSizeHint(PAGE_COUNT_MOVIES)
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(0)
                    .setMaxSize(MAX_SIZE_UNBOUNDED)
                    .build()
                RxPagedListBuilder(moviesDataSourceFactory, config)
                    .buildObservable()
                    .map { movies ->
                        MoviesResults.LoadMoviesResult.Success(movies as PagedList<Any>)
                    }
                    .cast(MoviesResults.LoadMoviesResult::class.java)
                    .onErrorReturn(MoviesResults.LoadMoviesResult::Failure)
                    .subscribeOn(scheduler.io())
                    .observeOn(scheduler.ui())
                    .startWith(MoviesResults.LoadMoviesResult.InFlight)
            }

        }

    private val searchMoviesProcessor =
        ObservableTransformer<MoviesActions.SearchMovies, MoviesResults> { action ->
            action.flatMap { searchAction ->
                RxPagedListBuilder(
                    MoviesSearchDataSourceFactory(moviesRepo, searchAction.query),
                    PAGE_COUNT
                )
                    .buildObservable()
                    .map { movies ->
                        MoviesResults.LoadMoviesResult.Success(movies)
                    }
                    .cast(MoviesResults.LoadMoviesResult::class.java)
                    .onErrorReturn(MoviesResults.LoadMoviesResult::Failure)
                    .subscribeOn(scheduler.io())
                    .observeOn(scheduler.ui())
                    .startWith(MoviesResults.LoadMoviesResult.InFlight)

            }

        }
}