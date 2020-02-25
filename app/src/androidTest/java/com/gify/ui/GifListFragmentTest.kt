package com.gify.ui

import Utils
import android.text.format.DateUtils
import android.widget.EditText
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gify.R
import com.gify.data.network.GifAPI
import com.gify.data.network.response.GifResponse
import com.gify.util.ElapsedTimeIdlingResource
import com.gify.util.EspressoCountingIdlingResource
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import viewassertion.RecyclerViewItemCountAssertion
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class GifListFragmentTest {

    lateinit var gifAPI: GifAPI
    val testSubscriber = TestSubscriber<GifResponse>()

    lateinit var mockServer: MockWebServer

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoCountingIdlingResource.idlingResource)
        mockServer = MockWebServer()
        mockServer.start(8080)
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(mockServer.url("/").toString())
            .build()
        gifAPI = retrofit.create(GifAPI::class.java)

    }


    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoCountingIdlingResource.idlingResource)
        mockServer.shutdown()
    }


    @Test
    fun should_show_empty_view__on_activity_launched() {
        onView(withId(R.id.emptyView))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun should_show_empty_view_for_empty_keyword_search() {
        onView(withId(R.id.action_search))
            .perform(click())

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("android"), clearText(), pressImeActionButton())

        onView(withId(R.id.emptyView))
            .check(ViewAssertions.matches(isDisplayed()))

    }

    @Test
    fun should_show_gifs_when_search_for_valid_keyword() {
        onView(withId(R.id.action_search))
            .perform(click())

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("android"), pressImeActionButton())
        //Thread.sleep(2000)
        //when
        mockHttpresponse("test_data.json", HttpURLConnection.HTTP_OK)
        gifAPI.search("android", 20, 0).subscribe(testSubscriber)
        // Start


        val idlingResource = waitFor(DateUtils.SECOND_IN_MILLIS * 5)

        // Clean up
        onView(withId(R.id.gifList))
            .check(ViewAssertions.matches(isDisplayed()))
        IdlingRegistry.getInstance().unregister(idlingResource)

    }


    @Test
    fun should_show_20_gifs_per_request_for_valid_keyword() {
        onView(withId(R.id.action_search))
            .perform(click())

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("android"), pressImeActionButton())
        Thread.sleep(2000)

        onView(withId(R.id.gifList))
            .check(RecyclerViewItemCountAssertion.hasItemCount(20))


        onView(withId(R.id.gifList))
            .check(ViewAssertions.matches(isDisplayed()))

    }

    @Test
    fun should_open_Gif_details_when_click_on_list_item() {
        onView(withId(R.id.action_search))
            .perform(click())

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("android"), pressImeActionButton())
        Thread.sleep(2000)

        onView(withId(R.id.gifList))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.gifList))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<GifyListAdapter.GifViewHolder>(
                    5,
                    click()
                )
            )
    }


    open fun mockHttpresponse(filename: String, responseCode: Int) = mockServer.enqueue(
        MockResponse()
            .setResponseCode(responseCode)
            .setBody(getJson(filename))
    )

    fun mockHttpResponse(mockResponse: MockResponse) = mockServer.enqueue(mockResponse)

    fun getJson(path: String): String {
        val context = InstrumentationRegistry.getContext()
        val stream = context.resources.assets.open(path)
        return Utils.readTextStream(stream)
    }


    private fun waitFor(waitingTime: Long): IdlingResource {

        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(waitingTime * 2, TimeUnit.MILLISECONDS)
        IdlingPolicies.setIdlingResourceTimeout(waitingTime * 2, TimeUnit.MILLISECONDS)

        // Now we wait
        val idlingResource = ElapsedTimeIdlingResource(waitingTime)
        IdlingRegistry.getInstance().register(idlingResource)
        return idlingResource
    }


}