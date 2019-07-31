package com.ahmed3elshaer.moviesdecade.moviedetail

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ahmed3elshaer.moviesdecade.R
import com.ahmed3elshaer.moviesdecade.data.models.Movie
import com.ahmed3elshaer.moviesdecade.movies.MoviesViewModel
import com.ahmed3elshaer.moviesdecade.mvibase.MviView
import com.ahmed3elshaer.moviesdecade.utils.MOVIE_KEY
import com.ahmed3elshaer.moviesdecade.utils.ViewModelFactory
import com.ahmed3elshaer.moviesdecade.utils.hide
import com.ahmed3elshaer.moviesdecade.utils.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.movie_detail_sheet.*
import javax.inject.Inject


class MovieDetailsBottomSheet : BottomSheetDialogFragment(),
    MviView<DetailsIntents, DetailsViewStates> {


    private val disposables = CompositeDisposable()
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: DetailsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(DetailsViewModel::class.java)
    }

    override fun intents(): Observable<DetailsIntents> {
        return Observable.just(DetailsIntents.QueryIntent(movie.title))
    }

    override fun render(state: DetailsViewStates) {
        loadingState(state.isLoading)
        if (state.error != null) {
            showMessage(state.error.message)
            state.error.printStackTrace()
            return
        }
        galleryAdapter.swapData(state.urls)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bind()
    }

    private fun bind() {
        disposables.add(viewModel.states().subscribe(this::render))
        // Pass the UI's intents to the ViewModel
        viewModel.processIntents(intents())
    }

    private fun loadingState(isLoading: Boolean) {
        if (isLoading)
            animation_view?.show()
        else
            animation_view?.hide()

    }

    private fun showMessage(message: String?) {
        if (view != null && message != null)
            Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
                .show()


    }

    private lateinit var movie: Movie
    private val galleryAdapter = ImagesAdapter()

    companion object {
        fun newInstance(movie: Movie): MovieDetailsBottomSheet {
            val bundle = Bundle()
            bundle.putSerializable(MOVIE_KEY, movie)
            with(MovieDetailsBottomSheet()) {
                arguments = bundle
                return this
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogStyle)
        movie = arguments!![MOVIE_KEY] as Movie

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.movie_detail_sheet, container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(movie)
        initImages()

    }



    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)


    }

    private fun initImages() {
        rvGallery?.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        rvGallery?.itemAnimator = DefaultItemAnimator()
        rvGallery?.setHasFixedSize(true)
        rvGallery?.adapter = galleryAdapter
    }

    private fun bindViews(movie: Movie) {
        with(movie) {
            if (cast.isEmpty()) {
                tvCast.hide()
                tvCastTitle.hide()
            }
            if (genres.isEmpty()) {
                tvGenres.hide()
                tvGenreTitle.hide()
            }

            val titleSpan = SpannableString("$title ($year)")
            titleSpan.setSpan(
                ForegroundColorSpan(Color.parseColor("#ABABAB")),
                title.length,
                titleSpan.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tvTitle?.text = titleSpan
            val castStr = StringBuilder()
            cast.forEach {
                castStr.append(it)
                if (it != cast.last())
                    castStr.append(", ")
            }
            tvCast?.text = castStr.toString()
            val genreStr = StringBuilder()
            genres.forEach {
                genreStr.append(it)
                if (it != genres.last())
                    genreStr.append(", ")
            }
            tvGenres?.text = genreStr.toString()
            ratingBar?.rating = rating.toFloat()
            ratingBar?.isEnabled = false
        }

    }

}