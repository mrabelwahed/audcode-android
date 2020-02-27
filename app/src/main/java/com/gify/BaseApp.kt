package com.gify

import android.app.Application
import com.gify.AppConst.BASE_URL
import com.gify.di.component.AppComponent
import com.gify.di.component.DaggerAppComponent
import com.gify.di.module.NetworkModule
import com.gify.di.module.GifyUsecaseModule
import com.gify.di.module.RepositoryModule

open class BaseApp : Application() {
     val appComponent: AppComponent by lazy { createAppComponent() }



   open  fun createAppComponent()  = DaggerAppComponent.builder()
        .networkModule(NetworkModule(BASE_URL))
        .repositoryModule(RepositoryModule())
        .gifyUsecaseModule(GifyUsecaseModule())
        .build()

     override fun onCreate() {
          super.onCreate()
          createAppComponent().inject(this)
     }



}