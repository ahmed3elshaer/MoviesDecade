package com.ahmed3elshaer.moviesdecade.movies

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmed3elshaer.moviesdecade.R
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.mvibase.MviView
import com.ahmed3elshaer.moviesdecade.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MviView<MoviesIntents, MoviesViewStates>,
    HasActivityInjector {
    @Inject

    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }

    private val disposables = CompositeDisposable()
    @Inject
    lateinit var viewModelFactory: MoviesViewModelFactory

    private val viewModel: MoviesViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders
            .of(this,viewModelFactory)
            .get(MoviesViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun render(state: MoviesViewStates) {

    }


    override fun intents(): Observable<MoviesIntents> {
        return Observable.merge(initIntent(),
            searchIntent())
    }

    private fun initIntent(): Observable<MoviesIntents.InitIntent> {
        return Observable.just(MoviesIntents.InitIntent)
    }

    private fun searchIntent(): Observable<MoviesIntents.SearchIntent> {
        return RxSearchObservable.fromView(svMovies)
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter {
                it.isNotEmpty()
            }
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                MoviesIntents.SearchIntent(it)
            }

    }
}
