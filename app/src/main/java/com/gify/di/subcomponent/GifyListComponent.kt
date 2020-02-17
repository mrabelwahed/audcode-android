package com.gify.di.subcomponent

import com.gify.di.module.GifyListViewModelModule
import com.gify.di.module.ViewModelFactoryModule
import com.gify.di.scope.FragmentScope
import com.gify.ui.GifListFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(
    modules = [
        ViewModelFactoryModule::class,
        GifyListViewModelModule::class
    ]
)
interface GifyListComponent {
    fun inject(gifListFragment: GifListFragment)
}