package com.gify.ui

import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gify.R
import com.gify.util.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import viewassertion.RecyclerViewItemCountAssertion


@RunWith(AndroidJUnit4::class)
class GifListFragmentTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
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
        Thread.sleep(2000)
        onView(withId(R.id.gifList))
            .check(ViewAssertions.matches(isDisplayed()))
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


}