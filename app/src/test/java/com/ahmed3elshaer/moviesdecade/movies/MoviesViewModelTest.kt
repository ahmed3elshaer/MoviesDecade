package com.ahmed3elshaer.moviesdecade.movies

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.utils.schedulers.BaseSchedulerProvider
import com.ahmed3elshaer.moviesdecade.utils.schedulers.ImmediateSchedulerProvider
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MoviesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = CountingTaskExecutorRule()

    @Mock
    private lateinit var moviesRepository: MoviesRepository
    private lateinit var schedulerProvider: BaseSchedulerProvider
    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var moviesActionProcessor: FakeMoviesActionProcessor
    private lateinit var testObserver: TestObserver<MoviesViewStates>



    @Before
    fun setUpMovieViewModel() {
        MockitoAnnotations.initMocks(this)
        schedulerProvider = ImmediateSchedulerProvider()
        moviesActionProcessor = FakeMoviesActionProcessor(moviesRepository, schedulerProvider)
        moviesViewModel =
            MoviesViewModel(moviesActionProcessor)
        testObserver = moviesViewModel.states()
            .test()
    }

    @Test
    fun loadMovies() {
        //triggering fake Init Intent
        moviesViewModel.processIntents(Observable.just(MoviesIntents.InitIntent))
        // Then progress indicator state is emitted
        testObserver.assertValueAt(1, MoviesViewStates::isLoading)
        // Then progress indicator state is canceled and all movies are emitted
        testObserver.assertValueAt(2) { movieViewState ->
            movieViewState.movies?.size == 3
                    && !movieViewState.isLoading
        }

    }

    @Test
    fun searchMovies() {
        //triggering fake Init Intent
        moviesViewModel.processIntents(Observable.just(MoviesIntents.SearchIntent("1")))
        // Then progress indicator state is emitted
        testObserver.assertValueAt(1, MoviesViewStates::isLoading)
        // Then progress indicator state is canceled and all movies are emitted
        testObserver.assertValueAt(2) { movieViewState ->
            movieViewState.movies?.size == 5
                    && !movieViewState.isLoading
        }

    }
}