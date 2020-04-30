package com.audcode.di.component

import com.audcode.BaseApp
import com.audcode.di.module.*
import com.audcode.di.scope.AppScope
import com.audcode.di.subcomponent.HomeComponent
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
    fun inject(app:BaseApp)
}