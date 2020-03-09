package com.audcode.di.module

import com.audcode.di.scope.AppScope
import com.audcode.data.network.GifAPI
import com.audcode.data.repository.GifDataRepository
import dagger.Module
import dagger.Provides

@Module
open class RepositoryModule {
    @AppScope
    @Provides
   open fun provideGifRepository(api: GifAPI) =
        GifDataRepository(api)
}