package com.abelvolpi.pokemonapi.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abelvolpi.pokemonapi.data.models.GenericPokemon
import com.abelvolpi.pokemonapi.data.models.PokemonListResponse
import com.abelvolpi.pokemonapi.domain.usecase.GetPokemonListUseCase
import com.abelvolpi.pokemonapi.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase
) : ViewModel() {

    var offSet: Int = 0
    private val _newPokemonsListState = MutableStateFlow<UiState<PokemonListResponse>>(UiState.Loading)
    val newPokemonsListState: StateFlow<UiState<PokemonListResponse>> = _newPokemonsListState

    private val _pokemonList = mutableListOf<GenericPokemon>()
    val pokemonList: List<GenericPokemon> get() = _pokemonList

    fun fetchPokemonList(offset: Int?, limit: Int?) {
        viewModelScope.launch {
            try {
                _newPokemonsListState.value = UiState.Loading
                val response = getPokemonListUseCase(offset, limit)
                _newPokemonsListState.value = UiState.Success(response)

                _pokemonList.addAll(response.results)
            } catch (e: Throwable) {
                _newPokemonsListState.value = UiState.Failure(e)
            }
        }
    }
}
