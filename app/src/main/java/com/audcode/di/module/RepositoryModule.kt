package com.audcode.di.module

import com.audcode.data.network.EpisodeApi
import com.audcode.data.repository.EpisodeDataRepository
import com.audcode.di.scope.AppScope
import dagger.Module
import dagger.Provides

@Module
open class RepositoryModule {
    @AppScope
    @Provides
    open fun provideGifRepository(api: EpisodeApi) =
        EpisodeDataRepository(api)
}