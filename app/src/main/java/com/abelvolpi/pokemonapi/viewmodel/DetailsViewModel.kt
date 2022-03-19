package com.abelvolpi.pokemonapi.viewmodel

import androidx.lifecycle.MutableLiveData
import com.abelvolpi.pokemonapi.api.ApiResponse
import com.abelvolpi.pokemonapi.api.repository.PokemonRepository
import com.abelvolpi.pokemonapi.models.DetailedPokemon
import kotlinx.coroutines.launch

class DetailsViewModel : CoroutineViewModel() {

    fun getPokemonDetails(pokemonName: String): MutableLiveData<ApiResponse<DetailedPokemon>> {
        val mutableLiveData = MutableLiveData<ApiResponse<DetailedPokemon>>()

        launch {
            setIsLoading()
            try {
                val pokemonDetailed = PokemonRepository.getPokemonInfo(pokemonName)
                mutableLiveData.value = ApiResponse.Success(pokemonDetailed)
            } catch (throwable: Throwable) {
                mutableLiveData.value = ApiResponse.Failure(throwable)
            }
            setIsNotLoading()
        }

        return mutableLiveData
    }
}