package com.gify

import com.gify.di.component.DaggerAppComponent
import com.gify.di.module.GifyUsecaseModule
import com.gify.di.module.RepositoryModule
import com.gify.di.module.TestingApplicationModule

class UITestApp : BaseApp() {

    override fun initDagger() = DaggerAppComponent.builder()
        .networkModule(TestingApplicationModule())
        .repositoryModule(RepositoryModule())
        .gifyUsecaseModule(GifyUsecaseModule())
        .build()
}