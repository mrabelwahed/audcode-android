package com.audcode

import com.audcode.AppConst.BASE_URL
import com.audcode.di.component.DaggerTestAppComponent
import com.audcode.di.module.*
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
