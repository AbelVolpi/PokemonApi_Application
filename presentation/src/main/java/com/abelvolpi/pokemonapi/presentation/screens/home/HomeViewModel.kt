package com.abelvolpi.pokemonapi.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abelvolpi.pokemonapi.domain.usecase.GetPokemonListUseCase
import com.abelvolpi.pokemonapi.presentation.UiState
import com.abelvolpi.pokemonapi.presentation.mappers.toUiModel
import com.abelvolpi.pokemonapi.presentation.models.GenericPokemonUiModel
import com.abelvolpi.pokemonapi.presentation.models.PokemonListUiModel
import com.abelvolpi.pokemonapi.presentation.utils.Constants.LIMIT_PER_REQUEST
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase
) : ViewModel() {

    private var offset: Int = 0

    private val _newPokemonsListState = MutableStateFlow<UiState<PokemonListUiModel>>(UiState.Loading)
    val newPokemonsListState: StateFlow<UiState<PokemonListUiModel>> = _newPokemonsListState

    private val _pokemonList = mutableListOf<GenericPokemonUiModel>()
    val pokemonList: List<GenericPokemonUiModel> get() = _pokemonList

    fun fetchPokemonList() {
        viewModelScope.launch {
            try {
                _newPokemonsListState.value = UiState.Loading
                val response = getPokemonListUseCase(offset, LIMIT_PER_REQUEST).toUiModel()
                _newPokemonsListState.value = UiState.Success(response)

                offset += LIMIT_PER_REQUEST
                _pokemonList.addAll(response.results)
            } catch (e: Throwable) {
                _newPokemonsListState.value = UiState.Failure(e)
            } finally {
                _newPokemonsListState.value = UiState.Finished
            }
        }
    }
}
