package com.gify.di.component

import com.gify.di.module.*
import com.gify.ui.GifListFragmentTest
import dagger.Component
import com.gify.di.scope.AppScope

@AppScope
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        GifyUsecaseModule::class
    ]
)
interface TestAppComponent : AppComponent {
    fun inject(fragment: GifListFragmentTest)
}