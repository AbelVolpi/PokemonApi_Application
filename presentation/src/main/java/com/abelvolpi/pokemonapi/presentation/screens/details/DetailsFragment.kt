package com.abelvolpi.pokemonapi.presentation.screens.details

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import com.abelvolpi.pokemonapi.presentation.R
import com.abelvolpi.pokemonapi.presentation.UiState
import com.abelvolpi.pokemonapi.presentation.adapters.PokemonTypeAdapter
import com.abelvolpi.pokemonapi.presentation.databinding.FragmentDetailsBinding
import com.abelvolpi.pokemonapi.presentation.models.CustomImage
import com.abelvolpi.pokemonapi.presentation.models.DetailedPokemonUiModel
import com.abelvolpi.pokemonapi.presentation.models.GenericPokemonUiModel
import com.abelvolpi.pokemonapi.presentation.screens.BaseFragment
import com.abelvolpi.pokemonapi.presentation.utils.getBundleParcelable
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(FragmentDetailsBinding::inflate) {

    private val detailsViewModel: DetailsViewModel by viewModels()
    private val navController by lazy {
        findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpSharedElementAnimation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observePokemonDetailsState()
    }

    private fun setUpSharedElementAnimation() {
        val animation =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        postponeEnterTransition(200, TimeUnit.MILLISECONDS)
    }

    private fun initViews() {
        with(binding) {
            val argsGenericPokemon = arguments?.getBundleParcelable<GenericPokemonUiModel>("generic_pokemon")
            val argsImage = arguments?.getBundleParcelable<CustomImage>("pokemon_image")

            argsGenericPokemon?.let { genericPokemon ->
                fetchPokemonDetails(genericPokemon.name)
                pokemonNumberTextView.text =
                    getString(R.string.pokemon_number_pattern, genericPokemon.number)

                pokemonImageView.transitionName = genericPokemon.number
                pokemonImageView.doOnPreDraw {
                    startPostponedEnterTransition()
                }
            }

            argsImage?.let { pokemonImage ->
                pokemonImageView.setImageBitmap(pokemonImage.image)
                Palette.Builder(pokemonImage.image).generate { palette ->
                    palette?.dominantSwatch?.rgb?.let {
                        mainLayout.setBackgroundColor(it)
                    }
                }
            }

            arrowBack.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    private fun observePokemonDetailsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            detailsViewModel.pokemonDetailsState.collect { state ->
                when (state) {
                    is UiState.Loading -> showLoadingState()
                    is UiState.Success -> updateScreen(state.data)
                    is UiState.Failure -> showErrorFeedback()
                }
            }
        }
    }

    private fun fetchPokemonDetails(pokemonName: String) {
        detailsViewModel.fetchPokemonDetails(pokemonName)
    }

    private fun showLoadingState() {
        with(binding) {
            pokemonDetailsLayout.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun updateScreen(pokemon: DetailedPokemonUiModel) {
        with(binding) {
            progressBar.visibility = View.GONE
            pokemonDetailsLayout.visibility = View.VISIBLE

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
            val hpValue = pokemon.stats.firstOrNull { it.name == "hp" }?.value ?: 0
            val attackValue = pokemon.stats.firstOrNull { it.name == "attack" }?.value ?: 0
            val defenseValue = pokemon.stats.firstOrNull { it.name == "defense" }?.value ?: 0
            val speedValue = pokemon.stats.firstOrNull { it.name == "speed" }?.value ?: 0
            hpProgressBar.setProgress(hpValue)
            attackProgressBar.setProgress(attackValue)
            defenseProgressBar.setProgress(defenseValue)
            speedProgressBar.setProgress(speedValue)
        }
    }

    private fun showErrorFeedback() {
        with(binding) {
            progressBar.visibility = View.GONE
            pokemonDetailsLayout.visibility = View.GONE

            Toast.makeText(requireContext(), "Couldn't load", Toast.LENGTH_SHORT).show()
        }
    }
}
