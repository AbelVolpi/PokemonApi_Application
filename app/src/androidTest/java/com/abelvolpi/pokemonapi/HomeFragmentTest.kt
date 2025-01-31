package com.abelvolpi.pokemonapi

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abelvolpi.pokemonapi.data.di.UrlModule
import com.abelvolpi.pokemonapi.presentation.R
import com.abelvolpi.pokemonapi.presentation.screens.home.HomeFragment
import com.abelvolpi.pokemonapi.utils.MockServerDispatcher
import com.abelvolpi.pokemonapi.utils.launchFragmentInHiltContainer
import com.abelvolpi.pokemonapi.utils.waitUntil
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(UrlModule::class)
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class HomeFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private val robot = HomeFragmentRobot()
    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        hiltRule.inject()
        mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
        mockWebServer.start(8080)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testPokemonListRendered() {
        launchFragmentInHiltContainer<HomeFragment>()
        robot.checkRecyclerViewIsDisplayed()
            .checkRecyclerViewHasAtLeastOneItem()
            .scrollRecyclerViewUntilEnd()
    }
}

class HomeFragmentRobot {

    fun checkRecyclerViewIsDisplayed() = apply {
        onView(withId(R.id.grid_recycler_view))
            .check(matches(isDisplayed()))
    }

    fun checkRecyclerViewHasAtLeastOneItem() = apply {
        onView(withId(R.id.grid_recycler_view))
            .perform(waitUntil(hasMinimumChildCount(1)))
    }

    fun scrollRecyclerViewUntilEnd() = apply {
        onView(withId(R.id.grid_recycler_view))
            .perform(swipeUp())
    }
}
