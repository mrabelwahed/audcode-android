package com.audcode.di.module

import com.audcode.data.repository.GifDataRepository
import com.audcode.domain.interactor.GetGifListUseCase
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock

@Module
class TestGifyUsecaseModule : GifyUsecaseModule() {
    @Provides
     fun provideUseCase(repository: GifDataRepository) =
        GetGifListUseCase(mock(GifDataRepository::class.java))

}