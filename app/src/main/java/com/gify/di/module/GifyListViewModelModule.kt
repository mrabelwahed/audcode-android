package com.gify.di.module

import androidx.lifecycle.ViewModel
import com.gify.ui.viewmodel.GifListViewModel
import com.gify.ui.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class GifyListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(GifListViewModel::class)
    internal abstract fun bindGifListViewModel(viewModel: GifListViewModel): ViewModel
}