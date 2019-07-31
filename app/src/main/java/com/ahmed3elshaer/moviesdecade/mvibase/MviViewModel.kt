package com.ahmed3elshaer.moviesdecade.mvibase

import io.reactivex.Observable

interface MviViewModel<I : MviIntent, S : MviViewState> {
    fun processIntents(intents: Observable<I>)

    fun states(): Observable<S>
}