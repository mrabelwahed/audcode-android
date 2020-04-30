package com.audcode.di.module

import com.audcode.di.scope.AppScope
import com.audcode.data.repository.EpisodeDataRepository
import com.audcode.data.repository.UserDataRepository
import com.audcode.domain.interactor.AuthenticateUser
import com.audcode.domain.interactor.CreateUser
import com.audcode.domain.interactor.GetEpisodes
import dagger.Module
import dagger.Provides

@Module
open class AudcodeUseCase {
    @AppScope
    @Provides
    open fun provideGetEpisodesUseCase(repository : EpisodeDataRepository) =
        GetEpisodes(repository)

    @AppScope
    @Provides
    open fun provideCreateUserUseCase(repository : UserDataRepository) =
        CreateUser(repository)

    @AppScope
    @Provides
    open fun provideAuthenticateUserUseCase(repository : UserDataRepository) =
        AuthenticateUser(repository)
}