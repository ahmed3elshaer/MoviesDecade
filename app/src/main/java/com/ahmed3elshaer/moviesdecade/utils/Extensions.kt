package com.ahmed3elshaer.moviesdecade.utils

import android.app.Activity
import android.content.res.AssetManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import androidx.core.content.ContextCompat.getSystemService



@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <U : Any, T : Iterable<U>> Single<T>.flatMapIterable(): Observable<U> {
    return this.flatMapObservable {
        Observable.fromIterable(it)
    }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T : Any, U : Any> Observable<T>.notOfType(clazz: Class<U>): Observable<T> {
    checkNotNull(clazz) { "clazz is null" }
    return filter { !clazz.isInstance(it) }
}

fun View.hide(){
    this.visibility = View.GONE
}


fun View.show(){
    this.visibility = View.VISIBLE
}
 fun View.invisible() {
     this.visibility = View.INVISIBLE
}

fun Activity.hideKeyboard(){
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = this.currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun AssetManager.readAssetsFile(fileName : String): String = open(fileName).bufferedReader().use{it.readText()}