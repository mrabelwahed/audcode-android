package com.audcode

import android.app.Application
import com.audcode.AppConst.BASE_URL
import com.audcode.di.component.AppComponent
import com.audcode.di.component.DaggerAppComponent
import com.audcode.di.module.NetworkModule
import com.audcode.di.module.GifyUsecaseModule
import com.audcode.di.module.RepositoryModule

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