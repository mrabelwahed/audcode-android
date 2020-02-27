package com.gify

import com.gify.AppConst.BASE_URL
import com.gify.di.component.AppComponent
import com.gify.di.component.DaggerAppComponent
import com.gify.di.component.DaggerTestAppComponent
import com.gify.di.module.*
import com.squareup.okhttp.mockwebserver.MockWebServer

class TestApp : BaseApp(){
    private val mockWebServer = MockWebServer()
    override fun createAppComponent()=
        DaggerTestAppComponent.builder()
             .networkModule(  //TestNetworkModule(mockWebServer.url("/").toString()))
                 TestNetworkModule(BASE_URL))
            .repositoryModule(TestRepositoryModule())
            .gifyUsecaseModule(TestGifyUsecaseModule())
            .build()

}
