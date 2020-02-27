package com.gify.di.module

import com.gify.data.repository.GifDataRepository
import com.gify.domain.interactor.GetGifListUseCase
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock

@Module
class TestGifyUsecaseModule : GifyUsecaseModule() {
    @Provides
     fun provideUseCase(repository: GifDataRepository) =
        GetGifListUseCase(mock(GifDataRepository::class.java))

}