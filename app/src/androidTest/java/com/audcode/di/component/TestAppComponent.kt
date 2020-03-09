package com.audcode.di.component

import com.audcode.di.module.*
import com.audcode.ui.GifListFragmentTest
import dagger.Component
import com.audcode.di.scope.AppScope

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