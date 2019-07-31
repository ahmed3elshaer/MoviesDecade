package com.ahmed3elshaer.moviesdecade.di

import android.content.Context
import com.ahmed3elshaer.moviesdecade.App
import com.ahmed3elshaer.moviesdecade.AppTest
import dagger.Module
import dagger.Provides

@Module
class AppTestModule() {



    @Provides
    fun provideContext(app: AppTest): Context {
        return app.applicationContext
    }


}