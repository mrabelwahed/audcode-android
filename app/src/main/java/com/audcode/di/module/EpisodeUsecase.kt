package com.audcode.di.module

import com.audcode.di.scope.AppScope
import com.audcode.data.repository.EpisodeDataRepository
import com.audcode.domain.interactor.GetEpisodesUC
import dagger.Module
import dagger.Provides

@Module
open class EpisodeUsecase {
    @AppScope
    @Provides
    open fun provideGifListCase(repository : EpisodeDataRepository) =
        GetEpisodesUC(repository)
}