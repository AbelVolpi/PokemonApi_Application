package com.abelvolpi.pokemonapi.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abelvolpi.pokemonapi.data.models.DetailedPokemon
import com.abelvolpi.pokemonapi.domain.usecase.GetPokemonDetailsUseCase
import com.abelvolpi.pokemonapi.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase
) : ViewModel() {

    private val _pokemonDetailsState = MutableStateFlow<UiState<DetailedPokemon>>(UiState.Loading)
    val pokemonDetailsState: StateFlow<UiState<DetailedPokemon>> = _pokemonDetailsState

    fun fetchPokemonDetails(pokemonName: String) {
        viewModelScope.launch {
            try {
                _pokemonDetailsState.value = UiState.Loading
                val details = getPokemonDetailsUseCase(pokemonName)
                _pokemonDetailsState.value = UiState.Success(details)
            } catch (e: Throwable) {
                _pokemonDetailsState.value = UiState.Failure(e)
            }
        }
    }
}
