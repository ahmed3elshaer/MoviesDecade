package com.ahmed3elshaer.moviesdecade.movies

import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.data.models.Image
import com.ahmed3elshaer.moviesdecade.moviedetail.DetailsActionProcessor
import com.ahmed3elshaer.moviesdecade.moviedetail.DetailsIntents
import com.ahmed3elshaer.moviesdecade.moviedetail.DetailsViewModel
import com.ahmed3elshaer.moviesdecade.moviedetail.DetailsViewStates
import com.ahmed3elshaer.moviesdecade.utils.schedulers.BaseSchedulerProvider
import com.ahmed3elshaer.moviesdecade.utils.schedulers.ImmediateSchedulerProvider
import com.nhaarman.mockito_kotlin.any
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class DetailsViewModelTest {
    @Mock
    private lateinit var moviesRepository: MoviesRepository
    private lateinit var schedulerProvider: BaseSchedulerProvider
    private lateinit var moviesViewModel: DetailsViewModel
    private lateinit var moviesActionProcessor: DetailsActionProcessor
    private lateinit var testObserver: TestObserver<DetailsViewStates>
    private val fakeImages = listOf(Image(), Image())

    @Before
    fun setUpMovieViewModel() {
        MockitoAnnotations.initMocks(this)
        schedulerProvider = ImmediateSchedulerProvider()
        moviesActionProcessor = DetailsActionProcessor(moviesRepository, schedulerProvider)
        moviesViewModel =
            DetailsViewModel(moviesActionProcessor)
        testObserver = moviesViewModel.states()
            .test()
    }

    @Test
    fun requestImagesFromTitle() {
        `when`(moviesRepository.queryImages(any())).thenReturn(Observable.just(fakeImages))
        //triggering fake Init Intent
        moviesViewModel.processIntents(Observable.just(DetailsIntents.QueryIntent("title")))
        // Then progress indicator state is emitted
        testObserver.assertValueAt(1, DetailsViewStates::isLoading)
        // Then progress indicator state is canceled and all movies are emitted
        testObserver.assertValueAt(2) { movieViewState ->
            movieViewState.urls?.size == 2
                    && !movieViewState.isLoading
        }

    }

}