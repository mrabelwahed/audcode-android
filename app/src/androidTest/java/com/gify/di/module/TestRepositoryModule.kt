package com.gify.di.module

import com.gify.data.network.GifAPI
import com.gify.data.repository.GifDataRepository
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock

@Module
class TestRepositoryModule  :RepositoryModule(){
    @Provides
     fun provideRepository(api: GifAPI) = GifDataRepository(mock(GifAPI::class.java))
}