package com.abelvolpi.pokemonapi.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abelvolpi.pokemonapi.MainApplication
import com.abelvolpi.pokemonapi.data.models.Type
import com.abelvolpi.pokemonapi.databinding.TypeAdapterItemBinding
import com.abelvolpi.pokemonapi.utils.parseTypeToColor
import java.util.*

class PokemonTypeAdapter(
    private val typeList: List<Type>
) : RecyclerView.Adapter<PokemonTypeAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: TypeAdapterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(type: Type) {
            with(binding) {
                typeTextView.text = type.type.typeName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                typeTextView.setBackgroundColor(type.parseTypeToColor(binding.root.context))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TypeAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            typeList[position]
        )
    }

    override fun getItemCount() = typeList.size
}
