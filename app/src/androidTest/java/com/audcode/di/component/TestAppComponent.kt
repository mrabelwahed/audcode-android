package com.audcode.di.component

import com.audcode.di.module.AudcodeUseCase
import com.audcode.di.module.NetworkModule
import com.audcode.di.module.RepositoryModule
import com.audcode.di.scope.AppScope
import dagger.Component

@AppScope
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        AudcodeUseCase::class
    ]
)
interface TestAppComponent : AppComponent {
    //fun inject(fragment: GifListFragmentTest)
}