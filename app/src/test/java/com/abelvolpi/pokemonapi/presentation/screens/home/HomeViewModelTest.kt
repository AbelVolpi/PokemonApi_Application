package com.abelvolpi.pokemonapi.presentation.screens.home

import com.abelvolpi.pokemonapi.MainDispatcherRule
import com.abelvolpi.pokemonapi.data.models.GenericPokemon
import com.abelvolpi.pokemonapi.data.models.PokemonListResponse
import com.abelvolpi.pokemonapi.domain.usecase.GetPokemonListUseCase
import com.abelvolpi.pokemonapi.presentation.UiState
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

    private lateinit var mockGetPokemonListUseCase: GetPokemonListUseCase
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    @Before
    fun setup() {
        mockGetPokemonListUseCase = mockk()
        viewModel = HomeViewModel(mockGetPokemonListUseCase)
    }

    @Test
    fun `should fetch pokemons list according to offset and limit`() = runTest {
        // Given
        val offset = 0
        val limit = 20
        val mockResponse = PokemonListResponse(
            results = listOf(
                GenericPokemon(name = "pikachu", url = "https://pokeapi.co/api/v2/pokemon/25/"),
                GenericPokemon(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")
            ),
            nextPageUrl = "next_url"
        )
        coEvery { mockGetPokemonListUseCase.invoke(offset, limit) } returns mockResponse

        // When
        viewModel.fetchPokemonList(offset, limit)

        // Then
        assert(viewModel.newPokemonsListState.value == UiState.Loading)
        advanceUntilIdle()
        assert(viewModel.newPokemonsListState.value == UiState.Success(mockResponse))
        coVerify(exactly = 1) { mockGetPokemonListUseCase.invoke(offset, limit) }
    }

    @Test
    fun `should return error when there's a failure in fetch list call`() = runTest {
        // Given
        val offset = 0
        val limit = 20
        val exception = Throwable("Network error")
        coEvery { mockGetPokemonListUseCase.invoke(offset, limit) } throws exception

        // When
        viewModel.fetchPokemonList(offset, limit)

        // Then
        assert(viewModel.newPokemonsListState.value == UiState.Loading)
        advanceUntilIdle()
        assert(viewModel.newPokemonsListState.value == UiState.Failure<Nothing>(exception))
        coVerify(exactly = 1) { mockGetPokemonListUseCase.invoke(offset, limit) }
    }
}
