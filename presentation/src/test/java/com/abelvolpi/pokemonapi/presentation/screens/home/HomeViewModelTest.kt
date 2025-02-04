package com.abelvolpi.pokemonapi.presentation.screens.home

import com.abelvolpi.pokemonapi.commontest.MainDispatcherRule
import com.abelvolpi.pokemonapi.domain.models.GenericPokemon
import com.abelvolpi.pokemonapi.domain.models.PokemonList
import com.abelvolpi.pokemonapi.domain.usecase.GetPokemonListUseCase
import com.abelvolpi.pokemonapi.presentation.UiState
import com.abelvolpi.pokemonapi.presentation.models.GenericPokemonUiModel
import com.abelvolpi.pokemonapi.presentation.models.PokemonListUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var getPokemonListUseCase: GetPokemonListUseCase
    private lateinit var homeViewModel: HomeViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    @Before
    fun setup() {
        getPokemonListUseCase = mockk()
        homeViewModel =
            HomeViewModel(getPokemonListUseCase)
    }

    @Test
    fun `should fetch pokemons list according to offset and limit`() = runTest {
        // Given
        val offset = 0
        val limit = 20
        val mockResponse = createMockPokemonList()
        val expectedUiModel = createExpectedPokemonListUiModel()
        coEvery { getPokemonListUseCase.invoke(offset, limit) } returns mockResponse

        // When
        homeViewModel.fetchPokemonList()

        // Then
        assert(homeViewModel.newPokemonsListState.value == UiState.Loading)
        advanceUntilIdle()
        assert(homeViewModel.newPokemonsListState.value == UiState.Success(expectedUiModel))
        coVerify(exactly = 1) { getPokemonListUseCase.invoke(offset, limit) }
    }

    @Test
    fun `should return error when there's a failure in fetch list call`() = runTest {
        // Given
        val offset = 0
        val limit = 20
        val exception = Throwable("Network error")
        coEvery { getPokemonListUseCase.invoke(offset, limit) } throws exception

        // When
        homeViewModel.fetchPokemonList()

        // Then
        assert(homeViewModel.newPokemonsListState.value == UiState.Loading)
        advanceUntilIdle()
        assert(homeViewModel.newPokemonsListState.value == UiState.Failure<Nothing>(exception))
        coVerify(exactly = 1) { getPokemonListUseCase.invoke(offset, limit) }
    }

    private fun createMockPokemonList() = PokemonList(
        results = listOf(
            GenericPokemon(
                name = "pikachu",
                url = "https://pokeapi.co/api/v2/pokemon/25/",
                number = "25",
                imageUrl = "image_url"
            ),
            GenericPokemon(
                name = "bulbasaur",
                url = "https://pokeapi.co/api/v2/pokemon/1/",
                number = "1",
                imageUrl = "image_url"
            )
        ),
        nextPageUrl = "next_url"
    )

    private fun createExpectedPokemonListUiModel() =
        PokemonListUiModel(
            results = listOf(
                GenericPokemonUiModel(
                    name = "pikachu",
                    number = "25",
                    imageUrl = "image_url"
                ),
                GenericPokemonUiModel(
                    name = "bulbasaur",
                    number = "1",
                    imageUrl = "image_url"
                )
            )
        )
}
