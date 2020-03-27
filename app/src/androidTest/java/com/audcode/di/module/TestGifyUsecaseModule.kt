package com.audcode.di.module

import com.audcode.data.repository.EpisodeDataRepository
import com.audcode.domain.interactor.GetEpisodesUC
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock

@Module
class TestGifyUsecaseModule : EpisodeUsecase() {
    @Provides
     fun provideUseCase(repository: EpisodeDataRepository) =
        GetEpisodesUC(mock(EpisodeDataRepository::class.java))

}