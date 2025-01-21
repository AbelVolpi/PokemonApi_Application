package com.abelvolpi.pokemonapi.presentation.adapters

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.abelvolpi.pokemonapi.presentation.databinding.HomeAdapterItemBinding
import com.abelvolpi.pokemonapi.presentation.models.CustomImage
import com.abelvolpi.pokemonapi.presentation.models.GenericPokemonUiModel
import com.abelvolpi.pokemonapi.presentation.utils.setImageUsingGlide

class HomeAdapter(
    private val onPokemonClick: (GenericPokemonUiModel?, CustomImage?, ImageView) -> Unit
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val pokemonList = arrayListOf<GenericPokemonUiModel>()

    inner class ViewHolder(private val binding: HomeAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genericPokemonUiModel: GenericPokemonUiModel) {
            with(binding) {
                pokemonNameTextView.text = genericPokemonUiModel.name.replaceFirstChar { it.titlecase() }
                pokemonImage.setImageUsingGlide(binding.root.context, genericPokemonUiModel.imageUrl)
                pokemonImage.transitionName = genericPokemonUiModel.number
                itemLayout.setOnClickListener {
                    onPokemonClick.invoke(
                        genericPokemonUiModel,
                        binding.pokemonImage.drawable?.let { CustomImage(it.toBitmap()) },
                        pokemonImage
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HomeAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            pokemonList[position]
        )
    }

    override fun getItemCount() = pokemonList.size

    fun addMorePokemon(newGenericPokemonUiModelList: List<GenericPokemonUiModel>) {
        val startIndex = pokemonList.size
        val lastIndex = newGenericPokemonUiModelList.size
        pokemonList.addAll(newGenericPokemonUiModelList)
        notifyItemRangeInserted(startIndex, lastIndex)
    }
}

class SpacesItemDecoration(private val space: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.top = space
        outRect.bottom = space
    }
}
