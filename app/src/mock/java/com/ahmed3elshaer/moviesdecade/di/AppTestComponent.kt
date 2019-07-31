package com.ahmed3elshaer.moviesdecade.di


import com.ahmed3elshaer.moviesdecade.App
import com.ahmed3elshaer.moviesdecade.AppTest
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppTestModule::class,
        MoviesApplicationTestModule::class]
)
@Singleton
interface AppTestComponent {

    @Component.Builder
    interface Builder {

        fun build(): AppTestComponent
        @BindsInstance
        fun application(application: AppTest): Builder
    }

    fun inject(app: AppTest)
}
