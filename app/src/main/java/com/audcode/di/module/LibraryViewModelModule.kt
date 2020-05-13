package com.audcode.di.module

import androidx.lifecycle.ViewModel
import com.audcode.ui.library.LibraryVM
import com.audcode.ui.login.RegisterVM
import com.audcode.ui.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class LibraryViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LibraryVM::class)
    internal abstract fun bindLibraryViewModel(viewModel: LibraryVM): ViewModel
}