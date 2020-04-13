package com.kirik88.lannister

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.kirik88.lannister.ui.MainActivity
import com.kirik88.lannister.ui.adapters.CharacterAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Test
    fun listItemClick() {
        onView(withId(R.id.list)).check(matches(isDisplayed()))

        onView(withId(R.id.list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CharacterAdapter.ViewHolder>(12, click()))

        onView(withText("Balon Greyjoy")).check(matches(isDisplayed()))
    }
}