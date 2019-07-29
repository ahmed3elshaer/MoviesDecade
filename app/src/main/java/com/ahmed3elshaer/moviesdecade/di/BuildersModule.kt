package com.ahmed3elshaer.moviesdecade.di

import com.ahmed3elshaer.moviesdecade.movies.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector(modules = [MoviesApplicationModule::class])
    internal abstract fun bindMainActivity(): MainActivity



}
