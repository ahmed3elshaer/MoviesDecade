package com.ahmed3elshaer.moviesdecade.movies

import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class MoviesActionProcessor(moviesRepo: MoviesRepository, scheduler: BaseSchedulerProvider) {

    var actionProcessor: ObservableTransformer<MoviesActions, MoviesResults> =
        ObservableTransformer { action ->
            action.publish { shared ->
                Observable.merge(
                    shared.ofType(MoviesActions.LoadMovies::class.java).compose(loadMoviesProcessor),
                    shared.ofType(MoviesActions.SearchMovies::class.java).compose(
                        searchMoviesProcessor
                    )
                )
            }
        }

    val loadMoviesProcessor =
        ObservableTransformer { action: Observable<MoviesActions.LoadMovies> ->
            action.flatMap { loadAction ->
                //TODO get movies from repo
                moviesRepo.getMovies()
                    .map { movies ->  }
            }

        }

    val loadMoviesProcessor =
        ObservableTransformer { action: Observable<MoviesActions.LoadMovies> ->
            action.flatMap { loadAction ->
                //TODO get movies from repo

            }

        }
}