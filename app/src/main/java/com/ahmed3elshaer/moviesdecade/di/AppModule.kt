package com.ahmed3elshaer.moviesdecade.di

import android.content.Context
import com.ahmed3elshaer.moviesdecade.App
import dagger.Module
import dagger.Provides

@Module
class AppModule() {


    @Provides
    fun provideContext(app: App): Context {
        return app.applicationContext
    }


}