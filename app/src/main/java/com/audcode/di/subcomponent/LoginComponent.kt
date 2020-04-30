package com.audcode.di.subcomponent

import com.audcode.di.module.LoginViewModelModule
import com.audcode.di.module.RegisterViewModelModule
import com.audcode.di.module.ViewModelFactoryModule
import com.audcode.di.scope.FragmentScope
import com.ramadan.login.LoginFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(
    modules = [
        ViewModelFactoryModule::class,
        LoginViewModelModule::class
    ]
)
interface LoginComponent {
    fun inject(loginFragment: LoginFragment)
}