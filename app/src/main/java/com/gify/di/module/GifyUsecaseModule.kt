package com.gify.di.module

import com.gify.di.scope.AppScope
import com.gify.data.repository.GifDataRepository
import com.gify.domain.interactor.GetGifListUseCase
import dagger.Module
import dagger.Provides

@Module
class GifyUsecaseModule {
    @AppScope
    @Provides
    fun provideGifListCase(repository : GifDataRepository) =
        GetGifListUseCase(repository)
}