package com.abelvolpi.pokemonapi.ui.screens

import android.os.Bundle
import android.view.View
import com.abelvolpi.pokemonapi.R
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import com.abelvolpi.pokemonapi.adapter.PokemonTypeAdapter
import com.abelvolpi.pokemonapi.api.ApiResponse
import com.abelvolpi.pokemonapi.databinding.FragmentDetailsBinding
import com.abelvolpi.pokemonapi.models.CustomImage
import com.abelvolpi.pokemonapi.models.DetailedPokemon
import com.abelvolpi.pokemonapi.models.GenericPokemon
import com.abelvolpi.pokemonapi.viewmodel.DetailsViewModel
import com.google.android.flexbox.*
import java.util.*

class DetailsFragment : BaseFragment<FragmentDetailsBinding>(FragmentDetailsBinding::inflate) {

    private val detailsViewModel: DetailsViewModel by viewModels()
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            val argsGenericPokemon = arguments?.getParcelable<GenericPokemon>("generic_pokemon")
            val argsImage = arguments?.getParcelable<CustomImage>("pokemon_image")

            argsGenericPokemon?.let { genericPokemon ->
                sendPokemonDetailsRequest(genericPokemon.name)
                pokemonNumberTextView.text =
                    getString(R.string.pokemon_number_pattern, genericPokemon.number)
            }

            argsImage?.let { pokemonImage ->
                pokemonImageView.setImageBitmap(pokemonImage.image)
                Palette.Builder(pokemonImage.image).generate { palette ->
                    palette?.dominantSwatch?.rgb?.let { it ->
                        mainLayout.setBackgroundColor(it)
                    }
                }
            }

            arrowBack.setOnClickListener {
                navController.popBackStack()
            }
            detailsViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    pokemonDetailsLayout.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                } else {
                    pokemonDetailsLayout.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun sendPokemonDetailsRequest(pokemonName: String) {
        detailsViewModel.getPokemonDetails(pokemonName).observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse) {
                is ApiResponse.Success -> {
                    updateScreen(apiResponse.body)
                }

                is ApiResponse.Failure -> {
                    showErrorFeedback()
                }
            }
        }
    }

    private fun updateScreen(pokemon: DetailedPokemon) {
        with(binding) {
            pokemonNameTextView.text =
                pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            pokemonHeightTextView.text = getString(R.string.height_pattern, (pokemon.height / 10))
            pokemonWeightTextView.text = getString(R.string.weight_pattern, (pokemon.weight / 10))

            typesRecyclerView.apply {
                val myLayoutManager = FlexboxLayoutManager(requireContext())
                myLayoutManager.justifyContent = JustifyContent.CENTER
                myLayoutManager.alignItems = AlignItems.CENTER
                myLayoutManager.flexDirection = FlexDirection.ROW
                myLayoutManager.flexWrap = FlexWrap.WRAP
                layoutManager = myLayoutManager
                adapter = PokemonTypeAdapter(pokemon.types)
            }
            val hpValue = pokemon.stats.firstOrNull { it.stat.name == "hp" }?.baseStat ?: 0
            val attackValue = pokemon.stats.firstOrNull { it.stat.name == "attack" }?.baseStat ?: 0
            val defenseValue = pokemon.stats.firstOrNull { it.stat.name == "defense" }?.baseStat ?: 0
            val speedValue = pokemon.stats.firstOrNull { it.stat.name == "speed" }?.baseStat ?: 0
            hpProgressBar.setProgress(hpValue)
            attackProgressBar.setProgress(attackValue)
            defenseProgressBar.setProgress(defenseValue)
            speedProgressBar.setProgress(speedValue)
        }
    }

    private fun showErrorFeedback() {
        with(binding) {

        }
    }

}