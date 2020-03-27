package com.audcode.di.component

import com.audcode.di.module.*
import dagger.Component
import com.audcode.di.scope.AppScope

@AppScope
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        EpisodeUsecase::class
    ]
)
interface TestAppComponent : AppComponent {
    //fun inject(fragment: GifListFragmentTest)
}