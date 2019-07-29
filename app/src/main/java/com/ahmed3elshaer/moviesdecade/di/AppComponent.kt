package com.ahmed3elshaer.moviesdecade.di


import com.ahmed3elshaer.moviesdecade.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppModule::class,
        BuildersModule::class,
        MoviesApplicationModule::class]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {

        fun build(): AppComponent
        @BindsInstance
        fun application(application: App): Builder
    }

    fun inject(app: App)
}
