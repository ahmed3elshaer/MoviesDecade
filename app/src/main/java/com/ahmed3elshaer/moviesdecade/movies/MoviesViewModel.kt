package com.ahmed3elshaer.moviesdecade.movies

import androidx.lifecycle.ViewModel
import com.ahmed3elshaer.moviesdecade.mvibase.MviViewModel
import com.ahmed3elshaer.moviesdecade.utils.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class MoviesViewModel(var moviesActionProcessor: MoviesActionProcessor) : ViewModel(),
    MviViewModel<MoviesIntents, MoviesViewStates> {

    /**
     * Proxy subject used to keep the stream alive even after the UI gets recycled.
     * This is basically used to keep ongoing events and the last cached State alive
     * while the UI disconnects and reconnects on config changes.
     */
    private val intentsSubject: PublishSubject<MoviesIntents> = PublishSubject.create()
    private val statesObservable: Observable<MoviesViewStates> = compose()
    private val disposables = CompositeDisposable()

    /**
     * take only the first ever InitialIntent and all intents of other types
     * to avoid reloading data on config changes
     */
    private val intentFilter: ObservableTransformer<MoviesIntents, MoviesIntents>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    shared.ofType(MoviesIntents.InitIntent::class.java).take(1),
                    shared.notOfType(MoviesIntents.InitIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<MoviesIntents>) {
        disposables.add(intents.subscribe(intentsSubject::onNext))
    }

    override fun states(): Observable<MoviesViewStates> = statesObservable

    private fun actionFromIntents(intents: MoviesIntents): MoviesActions {
        return when (intents) {
            is MoviesIntents.InitIntent -> MoviesActions.LoadMovies
            is MoviesIntents.SearchIntent -> if (intents.query.isEmpty())
                MoviesActions.LoadMovies
            else
                MoviesActions.SearchMovies(intents.query)

        }
    }


    private fun compose(): Observable<MoviesViewStates> {
        return intentsSubject
            .compose(intentFilter)
            .map(this::actionFromIntents)
            .compose(moviesActionProcessor.actionProcessor)
            // Cache each state and pass it to the reducer to create a new state from
            // the previous cached one and the latest Result emitted from the action processor.
            // The Scan operator is used here for the caching.
            .scan(MoviesViewStates.idle(), reducer)
            // When a reducer just emits previousState, there's no reason to call render. In fact,
            // redrawing the UI in cases like this can cause jank (e.g. messing up snackbar animations
            // by showing the same snackbar twice in rapid succession).
            .distinctUntilChanged()
            // Emit the last one event of the stream on subscription
            // Useful when a View rebinds to the ViewModel after rotation.
            .replay(1)
            // Create the stream on creation without waiting for anyone to subscribe
            // This allows the stream to stay alive even when the UI disconnects and
            // match the stream's lifecycle to the ViewModel's one.
            .autoConnect(0)
    }

    override fun onCleared() {
        disposables.dispose()
    }

    companion object {
        private val reducer = BiFunction { previousState: MoviesViewStates, result: MoviesResults ->
            when (result) {
                is MoviesResults.LoadMoviesResult -> when (result) {
                    is MoviesResults.LoadMoviesResult.Success -> previousState.copy(
                        movies = result.movies,
                        isLoading = false
                    )
                    is MoviesResults.LoadMoviesResult.Failure -> previousState.copy(
                        error = result.error,
                        isLoading = false
                    )
                    is MoviesResults.LoadMoviesResult.InFlight -> previousState.copy(isLoading = true)
                }
                is MoviesResults.searchMoviesResult -> when (result) {
                    is MoviesResults.searchMoviesResult.Success -> previousState.copy(
                        moviesSearch = result.movies,
                        isLoading = false
                    )
                    is MoviesResults.searchMoviesResult.Failure -> previousState.copy(
                        error = result.error,
                        isLoading = false
                    )
                    is MoviesResults.searchMoviesResult.InFlight -> previousState.copy(isLoading = true)

                }
            }
        }


    }
}