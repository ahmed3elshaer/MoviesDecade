package com.ahmed3elshaer.moviesdecade.movies

import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import java.lang.Thread.sleep
import java.util.logging.Handler

class MoviesActionProcessor(moviesRepo: MoviesRepository, scheduler: BaseSchedulerProvider) {

    var actionProcessor =
        ObservableTransformer<MoviesActions,MoviesResults> { actions ->
            actions.publish { shared ->
                Observable.merge(
                    shared.ofType(MoviesActions.LoadMovies::class.java).compose(loadMoviesProcessor),
                    shared.ofType(MoviesActions.SearchMovies::class.java).compose(searchMoviesProcessor)
                )
            }
        }

    private val loadMoviesProcessor =
        ObservableTransformer<MoviesActions.LoadMovies,MoviesResults> { action ->
            action.flatMap {
                moviesRepo.getMovies()
                    .map { movies ->
                        MoviesResults.LoadMoviesResult.Success(movies)
                    }
                    .cast(MoviesResults.LoadMoviesResult::class.java)
                    .onErrorReturn (MoviesResults.LoadMoviesResult::Failure)
                    .subscribeOn(scheduler.io())
                    .observeOn(scheduler.ui())
                    .startWith(MoviesResults.LoadMoviesResult.InFlight)
            }

        }

    private val searchMoviesProcessor =
        ObservableTransformer<MoviesActions.SearchMovies,MoviesResults> { action ->
            action.flatMap { searchAction ->
                moviesRepo.searchMovies(searchAction.query)
                    .map { movies ->
                        MoviesResults.LoadMoviesResult.Success(movies)
                    }
                    .cast(MoviesResults.LoadMoviesResult::class.java)
                    .onErrorReturn (MoviesResults.LoadMoviesResult::Failure)
                    .subscribeOn(scheduler.io())
                    .observeOn(scheduler.ui())
                    .startWith(MoviesResults.LoadMoviesResult.InFlight)

            }

        }
}