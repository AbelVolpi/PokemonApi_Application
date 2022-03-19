package com.abelvolpi.pokemonapi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.abelvolpi.pokemonapi.databinding.HomeAdapterItemBinding
import com.abelvolpi.pokemonapi.models.CustomImage
import com.abelvolpi.pokemonapi.models.GenericPokemon
import com.abelvolpi.pokemonapi.utils.setImageUsingGlide

class HomeAdapter(
    private val onPokemonClick: (GenericPokemon?, CustomImage?) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val pokemonList = arrayListOf<GenericPokemon>()

    inner class ViewHolder(private val binding: HomeAdapterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(genericPokemon: GenericPokemon) {
            with(binding) {
                pokemonNameTextView.text = genericPokemon.name
                pokemonImage.setImageUsingGlide(context, genericPokemon.number)
                itemLayout.setOnClickListener {
                    onPokemonClick.invoke(
                        genericPokemon,
                        binding.pokemonImage.drawable?.let { CustomImage(it.toBitmap())}
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

    fun addMorePokemon(newGenericPokemonList: List<GenericPokemon>) {
        pokemonList.addAll(newGenericPokemonList)
        notifyDataSetChanged()
    }

}
