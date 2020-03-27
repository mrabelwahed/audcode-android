package com.audcode.di.subcomponent

import com.audcode.di.module.HomeViewModelModule
import com.audcode.di.module.ViewModelFactoryModule
import com.audcode.di.scope.FragmentScope
import com.audcode.ui.episode_details.EpisodeDetailsFragment
import com.audcode.ui.home.HomeFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(
    modules = [
        ViewModelFactoryModule::class,
        HomeViewModelModule::class
    ]
)
interface HomeComponent {
    fun inject(homeFragment: HomeFragment)
    fun inject(detailsFragment: EpisodeDetailsFragment)
}