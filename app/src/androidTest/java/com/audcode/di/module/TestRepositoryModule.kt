package com.audcode.di.module

import com.audcode.data.network.GifAPI
import com.audcode.data.repository.EpisodeDataRepository
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock

@Module
class TestRepositoryModule : RepositoryModule() {
    @Provides
    fun provideRepository(api: GifAPI) = EpisodeDataRepository(mock(GifAPI::class.java))
}