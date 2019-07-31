package com.ahmed3elshaer.moviesdecade.movies

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.data.paging.MoviesSearchDataSourceFactory
import com.ahmed3elshaer.moviesdecade.utils.PAGE_COUNT
import com.ahmed3elshaer.moviesdecade.utils.PAGE_COUNT_MOVIES
import com.ahmed3elshaer.moviesdecade.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

@Suppress("UNCHECKED_CAST")
open class MoviesActionProcessor(
    moviesRepo: MoviesRepository,
    scheduler: BaseSchedulerProvider
) {

    open var actionProcessor =
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
                RxPagedListBuilder(moviesRepo.allMoviesDataSource(), PAGE_COUNT_MOVIES)
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
                        MoviesResults.SearchMoviesResult.Success(movies)
                    }
                    .cast(MoviesResults.SearchMoviesResult::class.java)
                    .onErrorReturn(MoviesResults.SearchMoviesResult::Failure)
                    .subscribeOn(scheduler.io())
                    .observeOn(scheduler.ui())
                    .startWith(MoviesResults.SearchMoviesResult.InFlight)

            }

        }
}