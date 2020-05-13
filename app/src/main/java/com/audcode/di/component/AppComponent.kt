package com.audcode.di.component

import com.audcode.BaseApp
import com.audcode.di.module.AudcodeUseCase
import com.audcode.di.module.NetworkModule
import com.audcode.di.module.RepositoryModule
import com.audcode.di.scope.AppScope
import com.audcode.di.subcomponent.HomeComponent
import com.audcode.di.subcomponent.LibraryComponent
import com.audcode.di.subcomponent.LoginComponent
import com.audcode.di.subcomponent.RegisterComponent
import dagger.Component

@AppScope
@Component(
    modules = [
        NetworkModule::class,
        AudcodeUseCase::class,
        RepositoryModule::class
    ]
)
interface AppComponent {
    fun newHomeComponent(): HomeComponent
    fun newLoginComponent(): LoginComponent
    fun newRegisterComponent(): RegisterComponent
    fun newLibraryComponent(): LibraryComponent
    fun inject(app: BaseApp)
}