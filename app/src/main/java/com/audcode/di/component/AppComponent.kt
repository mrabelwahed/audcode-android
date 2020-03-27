package com.audcode.di.component

import com.audcode.BaseApp
import com.audcode.di.module.*
import com.audcode.di.scope.AppScope
import com.audcode.di.subcomponent.HomeComponent
import dagger.Component

@AppScope
@Component(
    modules = [
        NetworkModule::class,
        EpisodeUsecase::class,
        RepositoryModule::class
    ]
)
interface AppComponent {
    fun newHomeComponent(): HomeComponent
    fun inject(app:BaseApp)
}