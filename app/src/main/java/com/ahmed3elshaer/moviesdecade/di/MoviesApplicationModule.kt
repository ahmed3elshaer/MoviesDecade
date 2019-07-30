package com.ahmed3elshaer.moviesdecade.di

import android.content.Context
import com.ahmed3elshaer.moviesdecade.data.MoviesRepository
import com.ahmed3elshaer.moviesdecade.data.room.MoviesDao
import com.ahmed3elshaer.moviesdecade.data.room.MoviesDatabase
import com.ahmed3elshaer.moviesdecade.movies.MoviesActionProcessor
import com.ahmed3elshaer.moviesdecade.network.FlickerApi
import com.ahmed3elshaer.moviesdecade.utils.BASE_URL
import com.ahmed3elshaer.moviesdecade.utils.MoviesViewModelFactory
import com.ahmed3elshaer.moviesdecade.utils.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class MoviesApplicationModule() {

    @Provides
    internal fun provideViewModelFactory(
        actionProcessor: MoviesActionProcessor,
        context: Context
    ): MoviesViewModelFactory {
        return MoviesViewModelFactory(context, actionProcessor)
    }

    @Provides
    internal fun provideMoviesActionProcessor(
        repo: MoviesRepository
    ): MoviesActionProcessor {
        return MoviesActionProcessor(repo, SchedulerProvider)
    }

    @Provides
    internal fun provideMoviesRepo(
        moviesDao: MoviesDao,
        flickerApi: FlickerApi,
        context: Context
    ): MoviesRepository {
        return MoviesRepository(moviesDao,
            flickerApi,
            context
        )
    }


    @Provides
    internal fun provideMoviesDao(context: Context): MoviesDao {
        return MoviesDatabase.getInstance(context).moviesDao()
    }


    @Provides
    internal fun provideFlickerApi(retrofit: Retrofit): FlickerApi {
        return retrofit
            .create(FlickerApi::class.java)
    }


    @Provides
    internal fun provideRetrofitInterface(client: OkHttpClient): Retrofit {

        val mochi = MoshiConverterFactory.create()
        mochi.asLenient()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(mochi)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }


}