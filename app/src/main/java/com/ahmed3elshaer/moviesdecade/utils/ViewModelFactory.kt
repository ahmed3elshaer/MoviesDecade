package com.ahmed3elshaer.moviesdecade.utils


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmed3elshaer.moviesdecade.moviedetail.DetailsActionProcessor
import com.ahmed3elshaer.moviesdecade.moviedetail.DetailsViewModel
import com.ahmed3elshaer.moviesdecade.movies.MoviesActionProcessor
import com.ahmed3elshaer.moviesdecade.movies.MoviesViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val applicationContext: Context,
    private val moviesActionProcessor: MoviesActionProcessor,
    private val detailsActionProcessor: DetailsActionProcessor
) :  ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass == MoviesViewModel::class.java) {
      return MoviesViewModel(
          moviesActionProcessor
         ) as T
    }else if (modelClass == DetailsViewModel::class.java){
        return DetailsViewModel(detailsActionProcessor) as T
    }

    throw IllegalArgumentException("unknown model class " + modelClass)
  }

}
