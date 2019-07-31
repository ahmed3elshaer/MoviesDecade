package com.ahmed3elshaer.moviesdecade.di

import com.ahmed3elshaer.moviesdecade.moviedetail.MovieDetailsBottomSheet
import com.ahmed3elshaer.moviesdecade.movies.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindDetailsFragment(): MovieDetailsBottomSheet


}
