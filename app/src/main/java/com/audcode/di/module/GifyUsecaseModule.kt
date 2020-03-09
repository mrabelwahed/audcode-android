package com.audcode.di.module

import com.audcode.di.scope.AppScope
import com.audcode.data.repository.GifDataRepository
import com.audcode.domain.interactor.GetGifListUseCase
import dagger.Module
import dagger.Provides

@Module
open class GifyUsecaseModule {
    @AppScope
    @Provides
    open fun provideGifListCase(repository : GifDataRepository) =
        GetGifListUseCase(repository)
}