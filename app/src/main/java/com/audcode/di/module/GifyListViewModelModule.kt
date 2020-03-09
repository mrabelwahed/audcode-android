package com.audcode.di.module

import androidx.lifecycle.ViewModel
import com.audcode.ui.viewmodel.GifListViewModel
import com.audcode.ui.viewmodel.ViewModelKey
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