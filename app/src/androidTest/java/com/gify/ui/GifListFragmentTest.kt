package com.gify.ui

import android.widget.EditText
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gify.*
import com.gify.data.network.GifAPI
import com.gify.data.network.response.GifResponse
import com.gify.viewassertion.RecyclerViewItemCountAssertion
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
class GifListFragmentTest {

    private val mockWebServer = MockWebServer()
    private val portNumber = 8080


    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)




@Inject
lateinit var  gifAPI:GifAPI
    @Before
    fun setup() {
        val app = InstrumentationRegistry.getTargetContext().applicationContext as TestApp
        app.createAppComponent().inject(this)
        mockWebServer.start(portNumber)
    }


    @After
    fun unregisterIdlingResource() {
        mockWebServer.shutdown()
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

//        val response = MockResponse()
//            .setResponseCode(200)
//            .setBody(getJson("test_data.json"))
//            .setBodyDelay(1, TimeUnit.SECONDS)
//
//        mockWebServer.enqueue(response)
//        val testSubscriber = TestSubscriber<GifResponse>()
//        gifAPI.search("android", 20, 0).subscribe(testSubscriber)
//        val d = testSubscriber.values()[0]


        // Clean up
        onView(withId(R.id.gifList))
            .check(ViewAssertions.matches(isDisplayed()))

    }


    @Test
    fun should_show_20_gifs_per_request_for_valid_keyword() {
        onView(withId(R.id.action_search))
            .perform(click())

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("android"), pressImeActionButton())

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


    fun getJson(path: String): String {
        val context = InstrumentationRegistry.getContext()
        val stream = context.resources.assets.open(path)
        return Utils.readTextStream(stream)
    }

}