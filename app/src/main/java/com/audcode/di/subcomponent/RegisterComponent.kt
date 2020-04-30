package com.audcode.di.subcomponent

import com.audcode.di.module.RegisterViewModelModule
import com.audcode.di.module.ViewModelFactoryModule
import com.audcode.di.scope.FragmentScope
import com.ramadan.login.RegisterFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(
    modules = [
        ViewModelFactoryModule::class,
        RegisterViewModelModule::class
    ]
)
interface RegisterComponent {
    fun inject(registerFragment: RegisterFragment)
}