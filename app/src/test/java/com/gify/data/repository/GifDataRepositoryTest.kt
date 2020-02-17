package com.gify.data.repository

import com.gify.data.network.GifAPI
import com.gify.data.network.response.GifResponse
import com.rules.RxSchedulerRule
import com.squareup.okhttp.mockwebserver.MockResponse
import com.util.BaseTest
import io.reactivex.subscribers.TestSubscriber
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class GifDataRepositoryTest : BaseTest() {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var testSchedulerRule: RxSchedulerRule = RxSchedulerRule()

    lateinit var repository: GifDataRepository
    lateinit var gifAPI: GifAPI

    val testSubscriber = TestSubscriber<GifResponse>()

    @Before
    override fun setup() {
        super.setup()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(mockServer.url("/").toString())
            .build()
        gifAPI = retrofit.create(GifAPI::class.java)
        repository = GifDataRepository(gifAPI)
    }

    override fun isMockServerEnable() = true


    @Test
    fun `repository is ready for test`() {
        assertNotNull(repository)
    }


    @Test
    fun ` search gif api should return list of 20 gifs per page `() {
        mockHttpresponse("get_gifs_for_egypt.json", HttpURLConnection.HTTP_OK)
        gifAPI.search("egypt", 20, 0).subscribe(testSubscriber)
        testSubscriber.assertNoErrors().assertComplete()
        val response = testSubscriber.values()[0]
        val returnedSize = response.data.size
        assertEquals("images size should be only 20 in this test case", returnedSize, 20)
    }

    @Test
    fun `search gif api should return server error when there is internal server error`() {
        this.mockHttpResponse(MockResponse().setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR))
        gifAPI.search("egypt", 20, 0).subscribe(testSubscriber)
        testSubscriber.assertNoValues().assertNotComplete()
        assertEquals(1, testSubscriber.errorCount())
    }
}