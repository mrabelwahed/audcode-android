package com.audcode.di.subcomponent

import com.audcode.di.module.LibraryAdapterModule
import com.audcode.di.module.LibraryViewModelModule
import com.audcode.di.module.RegisterViewModelModule
import com.audcode.di.module.ViewModelFactoryModule
import com.audcode.di.scope.FragmentScope
import com.audcode.ui.library.LibraryFragment
import com.ramadan.login.RegisterFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(
    modules = [
        ViewModelFactoryModule::class,
        LibraryAdapterModule::class,
        LibraryViewModelModule::class
    ]
)
interface LibraryComponent {
    fun inject(libraryFragment: LibraryFragment)
}