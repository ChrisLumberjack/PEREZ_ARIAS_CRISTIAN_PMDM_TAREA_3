package tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import tarea3.perez_arias_cristian_pmdm_tarea_3.R

// Adaptador para el RecyclerView que maneja la lista de Pokémon capturados
class PokemonAdapter(
    private val onItemClick: (Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    private val pokemons = mutableListOf<Pokemon>()

    // Función para actualizar la lista de Pokémon en el adaptador
    fun submitList(newList: List<Pokemon>) {
        pokemons.clear()
        pokemons.addAll(newList)
        notifyDataSetChanged() // Actualiza el RecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_pokemon, parent, false
        ) // Suponiendo que tienes un layout para el item
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemons[position]
        holder.bind(pokemon)  // Llamamos a bind para asignar los datos
    }

    override fun getItemCount(): Int = pokemons.size

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pokemonNameTextView: TextView = itemView.findViewById(R.id.pokemon_name)
        private val pokemonImageView: ImageView = itemView.findViewById(R.id.pokemon_image)

        fun bind(pokemon: Pokemon) {
            pokemonNameTextView.text = pokemon.name

            // Usamos Glide para cargar la imagen del Pokémon desde la URL
            val imageUrl = pokemon.photoUrl  // Usamos la URL almacenada en el objeto Pokemon

            // Si no tiene una URL personalizada, usamos la URL predeterminada por la API
            val finalImageUrl = imageUrl ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${getPokemonIndexFromUrl(pokemon.url)}.png"

            // Cargar la imagen con Glide
            Glide.with(itemView.context)
                .load(finalImageUrl)  // Cargar la imagen desde la URL proporcionada
                .into(pokemonImageView)  // Establecer la imagen en el ImageView

            // Detectar el clic en la card y llamar a onItemClick para capturar el Pokémon
            itemView.setOnClickListener {
                onItemClick(pokemon)  // Llamamos al callback (función capturePokemon) con el Pokémon clickeado
            }
        }


        // Función que obtiene el índice del Pokémon a partir de la URL
        private fun getPokemonIndexFromUrl(url: String): Int {
            val regex = Regex("pokemon/(\\d+)")
            val match = regex.find(url)
            return match?.groupValues?.get(1)?.toInt() ?: -1
        }
    }
}

