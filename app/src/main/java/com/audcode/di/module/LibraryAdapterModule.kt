package com.audcode.di.module

import com.audcode.di.scope.FragmentScope
import com.audcode.ui.LibraryAdapter
import dagger.Module
import dagger.Provides

@Module
class LibraryAdapterModule {
    @Provides
    @FragmentScope
    fun provideLibraryAdapter(): LibraryAdapter = LibraryAdapter()
}