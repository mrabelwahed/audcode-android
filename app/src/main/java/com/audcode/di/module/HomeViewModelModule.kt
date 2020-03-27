package com.audcode.di.module

import androidx.lifecycle.ViewModel
import com.audcode.ui.home.HomeVM
import com.audcode.ui.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class HomeViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeVM::class)
    internal abstract fun bindHomeViewModel(viewModel: HomeVM): ViewModel
}