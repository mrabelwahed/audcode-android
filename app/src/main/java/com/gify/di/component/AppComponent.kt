package com.gify.di.component

import com.gify.di.module.*
import com.gify.di.scope.AppScope
import com.gify.di.subcomponent.GifyListComponent
import dagger.Component

@AppScope
@Component(
    modules = [
        NetworkModule::class,
        GifyUsecaseModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {
    fun newGifLisComponent(): GifyListComponent
}