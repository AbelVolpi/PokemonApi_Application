package com.abelvolpi.pokemonapi.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abelvolpi.pokemonapi.presentation.UiState
import com.abelvolpi.pokemonapi.domain.usecase.GetPokemonListUseCase
import com.abelvolpi.pokemonapi.data.models.PokemonListResponse
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
    private val _pokemonListState = MutableStateFlow<UiState<PokemonListResponse>>(UiState.Loading)
    val pokemonListState: StateFlow<UiState<PokemonListResponse>> = _pokemonListState

    fun fetchPokemonList(offset: Int?, limit: Int?) {
        viewModelScope.launch {
            try {
                _pokemonListState.value = UiState.Loading
                val response = getPokemonListUseCase(offset, limit)
                _pokemonListState.value = UiState.Success(response)
            } catch (e: Throwable) {
                _pokemonListState.value = UiState.Failure(e)
            }
        }
    }
}