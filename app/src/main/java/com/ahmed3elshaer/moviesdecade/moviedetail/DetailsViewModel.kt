package com.ahmed3elshaer.moviesdecade.moviedetail

import androidx.lifecycle.ViewModel
import com.ahmed3elshaer.moviesdecade.mvibase.MviViewModel
import com.ahmed3elshaer.moviesdecade.utils.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class DetailsViewModel(var detailsActionProcessor: DetailsActionProcessor) : ViewModel(),
    MviViewModel<DetailsIntents, DetailsViewStates> {

    /**
     * Proxy subject used to keep the stream alive even after the UI gets recycled.
     * This is basically used to keep ongoing events and the last cached State alive
     * while the UI disconnects and reconnects on config changes.
     */
    private val intentsSubject: PublishSubject<DetailsIntents> = PublishSubject.create()
    private val statesObservable: Observable<DetailsViewStates> = compose()
    private val disposables = CompositeDisposable()

    /**
     * take only the first ever InitialIntent and all intents of other types
     * to avoid reloading data on config changes
     */
    private val intentFilter: ObservableTransformer<DetailsIntents, DetailsIntents>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    shared.ofType(DetailsIntents.QueryIntent::class.java).take(1),
                    shared.notOfType(DetailsIntents.QueryIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<DetailsIntents>) {
        disposables.add(intents.subscribe(intentsSubject::onNext))
    }

    override fun states(): Observable<DetailsViewStates> = statesObservable

    private fun actionFromIntents(intents: DetailsIntents): DetailsActions {
        return when (intents) {
            is DetailsIntents.QueryIntent -> DetailsActions.QueryImages(intents.query)

        }
    }


    private fun compose(): Observable<DetailsViewStates> {
        return intentsSubject
            .compose(intentFilter)
            .map(this::actionFromIntents)
            .compose(detailsActionProcessor.actionProcessor)
            // Cache each state and pass it to the reducer to create a new state from
            // the previous cached one and the latest Result emitted from the action processor.
            // The Scan operator is used here for the caching.
            .scan(DetailsViewStates.idle(), reducer)
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
        private val reducer =
            BiFunction { previousState: DetailsViewStates, result: DetailsResults ->
                when (result) {
                    is DetailsResults.QueryResult -> when (result) {
                        is DetailsResults.QueryResult.Success -> previousState.copy(
                            urls = result.urls,
                            isLoading = false
                        )
                        is DetailsResults.QueryResult.Failure -> previousState.copy(
                            error = result.error,
                            isLoading = false
                        )
                        is DetailsResults.QueryResult.InFlight -> previousState.copy(isLoading = true)
                    }
                }
            }


    }
}