package com.ahmed3elshaer.moviesdecade.movies

import androidx.paging.PagedList
import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.utils.mockPagedList
import com.ahmed3elshaer.moviesdecade.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

//the difference that we replace the
// ```
// RxPagedListBuilder(moviesRepo.allMoviesDataSource(), PAGE_COUNT_MOVIES)
// .buildObservable()
// ```
//in the real action processor as in testing RxPagedListBuilder doesn't seem to emmit
// values with many tires and researches i came up with solution of making fake PagedList providers in
// this FakeProcessor and use it in testing
@Suppress("UNCHECKED_CAST")
class FakeMoviesActionProcessor(
    moviesRepo: MoviesRepository,
    scheduler: BaseSchedulerProvider
) : MoviesActionProcessor(moviesRepo, scheduler) {
    override var actionProcessor =
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
                getFakeMoviesPagedListObservable()
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

    private fun getFakeMoviesPagedListObservable(): Observable<PagedList<Movie>> {
        return Observable.just(FakeMovies.movies.mockPagedList())
    }

    private fun getFakeMoviesSearchPagedListObservable(): Observable<PagedList<Any>> {
        return Observable.just(FakeMovies.moviesSearch.mockPagedList())
    }

    private val searchMoviesProcessor =
        ObservableTransformer<MoviesActions.SearchMovies, MoviesResults> { action ->
            action.flatMap { searchAction ->
                getFakeMoviesSearchPagedListObservable()
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