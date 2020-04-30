package com.audcode.di.module

import com.audcode.data.repository.EpisodeDataRepository
import com.audcode.domain.interactor.GetEpisodes
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock

@Module
class TestGifyUsecaseModule : AudcodeUseCase() {
    @Provides
     fun provideUseCase(repository: EpisodeDataRepository) =
        GetEpisodes(mock(EpisodeDataRepository::class.java))

}