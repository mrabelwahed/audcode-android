package com.audcode.di.subcomponent

import com.audcode.di.module.GifyListViewModelModule
import com.audcode.di.module.ViewModelFactoryModule
import com.audcode.di.scope.FragmentScope
import com.audcode.ui.GifListFragment
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