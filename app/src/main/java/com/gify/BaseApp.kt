package com.gify

import android.app.Application
import com.gify.di.component.AppComponent
import com.gify.di.component.DaggerAppComponent
import com.gify.di.module.NetworkModule
import com.gify.di.module.GifyUsecaseModule
import com.gify.di.module.RepositoryModule

open class BaseApp : Application() {
     val appComponent: AppComponent by lazy { initDagger() }


   open  fun initDagger()  = DaggerAppComponent.builder()
        .networkModule(NetworkModule())
        .repositoryModule(RepositoryModule())
        .gifyUsecaseModule(GifyUsecaseModule())
        .build()

}