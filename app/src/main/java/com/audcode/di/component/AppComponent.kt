package com.audcode.di.component

import com.audcode.BaseApp
import com.audcode.di.module.*
import com.audcode.di.scope.AppScope
import com.audcode.di.subcomponent.GifyListComponent
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
    fun inject(app:BaseApp)
}