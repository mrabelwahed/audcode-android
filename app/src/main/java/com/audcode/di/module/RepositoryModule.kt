package com.audcode.di.module

import com.audcode.data.network.AudcodeAPI
import com.audcode.data.repository.EpisodeDataRepository
import com.audcode.data.repository.UserDataRepository
import com.audcode.di.scope.AppScope
import dagger.Module
import dagger.Provides

@Module
open class RepositoryModule {
    @AppScope
    @Provides
    open fun provideEpisodeRepository(api: AudcodeAPI) =
        EpisodeDataRepository(api)

    @AppScope
    @Provides
    open fun provideUserRepository(api: AudcodeAPI) =
        UserDataRepository(api)
}