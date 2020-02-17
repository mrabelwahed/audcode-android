package com.gify.di.module

import com.gify.di.scope.AppScope
import com.gify.data.network.GifAPI
import com.gify.data.repository.GifDataRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @AppScope
    @Provides
    fun provideFeedRepository(api: GifAPI) =
        GifDataRepository(api)
}