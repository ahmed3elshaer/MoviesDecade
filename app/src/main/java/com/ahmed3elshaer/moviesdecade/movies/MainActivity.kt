package com.ahmed3elshaer.moviesdecade.movies

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmed3elshaer.moviesdecade.R
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.moviedetail.MovieDetailsBottomSheet
import com.ahmed3elshaer.moviesdecade.mvibase.MviView
import com.ahmed3elshaer.moviesdecade.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MviView<MoviesIntents, MoviesViewStates>,
    HasActivityInjector, HasSupportFragmentInjector {

    private val disposables = CompositeDisposable()
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MoviesViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(MoviesViewModel::class.java)
    }

    lateinit var moviesAdapter: MoviesAdapter
    lateinit var moviesSearchAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        moviesAdapter = MoviesAdapter()
        moviesSearchAdapter = MoviesAdapter()
        bind()
        initMoviesList()
        customizeSearchBar()

    }

    private fun customizeSearchBar() {
        (svMovies?.findViewById(androidx.appcompat.R.id.search_src_text) as TextView).apply {
            setTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
            val font = ResourcesCompat.getFont(context, R.font.circular_book);
            typeface = font
            setTextSize(TypedValue.COMPLEX_UNIT_SP,22f)

        }

    }

    private fun initMoviesList() {
        rvMovies?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvMovies?.itemAnimator = DefaultItemAnimator()
        rvMovies?.adapter = moviesAdapter
    }

    /**
     * Connect the [MviView] with the [MviViewModel]
     * We subscribe to the [MviViewModel] before passing it the [MviView]'s [MviIntent]s.
     * If we were to pass [MviIntent]s to the [MviViewModel] before listening to it,
     * emitted [MviViewState]s could be lost
     */
    private fun bind() {
        // Subscribe to the ViewModel and call render for every emitted state
        disposables.add(viewModel.states().subscribe(this::render))
        // Pass the UI's intents to the ViewModel
        viewModel.processIntents(intents())
        // for listening to open movie details
        detailsIntent()


    }

    override fun render(state: MoviesViewStates) {
        loadingState(state.isLoading)
        if (state.error != null) {
            showMessage(state.error.message)
            state.error.printStackTrace()
            return
        }
        state.movies?.let {
            when {
                state.movies.isEmpty() -> renderEmptyMovies()
                else -> renderMovies(state.movies, state.isSearch)
            }
        }


    }


    private fun renderMovies(movies: PagedList<Any>, isSearch: Boolean) {
        if (isSearch) {
            moviesSearchAdapter.submitList(movies)
            rvMovies?.adapter = moviesSearchAdapter
        } else {
            moviesAdapter.submitList(movies)
            rvMovies?.adapter = moviesAdapter
        }
        ivEmpty?.hide()
        tvEmpty?.hide()
        rvMovies?.show()


    }

    private fun renderEmptyMovies() {
        ivEmpty?.show()
        tvEmpty?.show()
        rvMovies?.hide()
    }

    private fun showMessage(message: String?) {
        message?.let {
            val view = findViewById<View>(android.R.id.content) ?: return
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .show()
        }

    }

    private fun loadingState(isLoading: Boolean) {
        if (isLoading)
            pbLoading?.show()
        else
            pbLoading?.invisible()


    }


    override fun intents(): Observable<MoviesIntents> {
        return Observable.merge(
            initIntent(),
            searchIntent()
        )
    }

    private fun initIntent(): Observable<MoviesIntents.InitIntent> {
        return Observable.just(MoviesIntents.InitIntent)
    }

    private fun searchIntent(): Observable<MoviesIntents.SearchIntent> {
        return RxSearchObservable.fromView(svMovies)
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                MoviesIntents.SearchIntent(it)
            }

    }

    private fun detailsIntent() {
        disposables.add(
            Observable.merge(
                moviesAdapter.movieClick,
                moviesSearchAdapter.movieClick
            ).subscribe { movie ->
                renderMovie(movie)
            }
        )
    }

    private fun renderMovie(movie: Movie) {
        MovieDetailsBottomSheet.newInstance(movie)
            .show(supportFragmentManager, MOVIE_DETAIL)
    }

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    override fun supportFragmentInjector(): AndroidInjector<androidx.fragment.app.Fragment> {
        return fragmentDispatchingAndroidInjector
    }

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

}



