package com.ahmed3elshaer.moviesdecade.moviedetail

import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.utils.schedulers.BaseSchedulerProvider
import io.reactivex.ObservableTransformer

@Suppress("UNCHECKED_CAST")
class DetailsActionProcessor(
    moviesRepo: MoviesRepository,
    scheduler: BaseSchedulerProvider
) {

    var actionProcessor =
        ObservableTransformer<DetailsActions, DetailsResults> { actions ->
            actions.publish { shared ->
                shared.ofType(DetailsActions.QueryImages::class.java).compose(queryImagesProcessor)

            }
        }

    private val queryImagesProcessor =
        ObservableTransformer<DetailsActions.QueryImages, DetailsResults> { action ->
            action.flatMap { queryAction ->
                moviesRepo.queryImages(queryAction.query)
                    .map { imageList ->
                        DetailsResults.QueryResult.Success(imageList.map { image ->
                            image.toUrl()
                        })
                    }
                    .cast(DetailsResults.QueryResult::class.java)
                    .onErrorReturn(DetailsResults.QueryResult::Failure)
                    .subscribeOn(scheduler.io())
                    .observeOn(scheduler.ui())
                    .startWith(DetailsResults.QueryResult.InFlight)
            }

        }


}