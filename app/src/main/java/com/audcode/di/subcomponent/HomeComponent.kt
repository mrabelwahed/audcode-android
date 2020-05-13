package com.audcode.di.subcomponent

import com.audcode.di.module.HomeViewModelModule
import com.audcode.di.module.ViewModelFactoryModule
import com.audcode.di.scope.FragmentScope
import com.audcode.ui.BaseFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(
    modules = [
        ViewModelFactoryModule::class,
        HomeViewModelModule::class
    ]
)
interface HomeComponent {
    fun inject(homeFragment: BaseFragment)
}