package com.ahmed3elshaer.moviesdecade.utils

import android.content.res.AssetManager
import android.view.View
import androidx.paging.PagedList
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.util.concurrent.Executors


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

fun View.hide() {
    this.visibility = View.GONE
}


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}


fun AssetManager.readAssetsFile(fileName: String): String =
    open(fileName).bufferedReader().use { it.readText() }

fun ioThread(f: () -> Unit) {
    Executors.newSingleThreadExecutor().execute(f)
}

fun <T> List<T>.mockPagedList(): PagedList<T> {
    val pagedList = Mockito.mock(PagedList::class.java) as PagedList<T>
    Mockito.`when`(pagedList.get(ArgumentMatchers.anyInt())).then { invocation ->
        val index = invocation.arguments.first() as Int
        this[index]
    }
    Mockito.`when`(pagedList.size).thenReturn(this.size)
    return pagedList
}
