package com.abelvolpi.pokemonapi

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abelvolpi.pokemonapi.data.di.UrlModule
import com.abelvolpi.pokemonapi.data.utils.Constants.URLS.PNG
import com.abelvolpi.pokemonapi.data.utils.Constants.URLS.POKEMON_IMAGE_URL
import com.abelvolpi.pokemonapi.presentation.R
import com.abelvolpi.pokemonapi.presentation.models.CustomImage
import com.abelvolpi.pokemonapi.presentation.models.GenericPokemonUiModel
import com.abelvolpi.pokemonapi.presentation.screens.details.DetailsFragment
import com.abelvolpi.pokemonapi.utils.MockServerDispatcher
import com.abelvolpi.pokemonapi.utils.UiTestUtils.loadBitmapFromAssets
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
class DetailsFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private val robot = DetailsFragmentRobot()
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
    fun testPokemonDetailsRendered() {
        val args = provideDetailsFragmentArgs()

        launchFragmentInHiltContainer<DetailsFragment>(fragmentArgs = args)
        robot.checkDetailsLayoutIsDisplayed()
            .checkPokemonImageIsDisplayed()
            .checkPokemonNameIsDisplayed()
    }

    private fun provideDetailsFragmentArgs(): Bundle {
        val bitmap = loadBitmapFromAssets("charizard.png")

        return bundleOf(
            "generic_pokemon" to GenericPokemonUiModel(
                name = "charizard",
                number = "#006",
                imageUrl = POKEMON_IMAGE_URL + "6" + PNG
            ),
            "pokemon_image" to CustomImage(
                image = bitmap
            )
        )
    }
}

class DetailsFragmentRobot {

    fun checkDetailsLayoutIsDisplayed() = apply {
        onView(withId(R.id.pokemon_details_layout))
            .perform(waitUntil(isDisplayed()))
    }

    fun checkPokemonImageIsDisplayed() = apply {
        onView(withId(R.id.pokemon_image_view))
            .perform(waitUntil(isDisplayed()))
    }

    fun checkPokemonNameIsDisplayed() = apply {
        onView(withId(R.id.pokemon_name_text_view))
            .perform(waitUntil(isDisplayed()))
    }
}
