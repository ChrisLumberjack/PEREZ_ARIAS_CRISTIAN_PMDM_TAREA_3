package tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import tarea3.perez_arias_cristian_pmdm_tarea_3.R

// Adaptador para el RecyclerView que maneja la lista de Pokémon capturados
class PokemonAdapter(
    private val onItemClick: (Pokemon) -> Unit,
    private val onDeleteClick: (Pokemon) -> Unit // Nuevo parámetro para manejar la eliminación
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {
    private var pokemonList = mutableListOf<Pokemon>()
    private var canDelete = false
    private val pokemons = mutableListOf<Pokemon>()

    // Función para actualizar la lista de Pokémon en el adaptador
    fun submitList(newList: List<Pokemon>) {
        pokemons.clear()
        pokemons.addAll(newList)
        notifyDataSetChanged() // Actualiza el RecyclerView
    }

    fun setPokemonList(pokemonList: List<Pokemon>) {
        this.pokemonList = pokemonList.toMutableList()
        notifyDataSetChanged()
    }

    fun setCanDelete(canDelete: Boolean) {
        this.canDelete = canDelete
        notifyDataSetChanged() // Notificar que los datos han cambiado para reflejar el estado de "delete"
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_pokemon, parent, false
        )
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemons[position]
        holder.bind(pokemon)
    }

    override fun getItemCount(): Int = pokemons.size

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pokemonNameTextView: TextView = itemView.findViewById(R.id.pokemon_name)
        private val pokemonImageView: ImageView = itemView.findViewById(R.id.pokemon_image)
        private val pokemonType: TextView = itemView.findViewById(R.id.pokemon_types)
        private val pokemonWeightTextView: TextView = itemView.findViewById(R.id.pokemon_weight)
        private val pokemonHeightTextView: TextView = itemView.findViewById(R.id.pokemon_height)
        private val deleteButton: Button = itemView.findViewById(R.id.btn_delete) // Asumiendo que tienes un botón para eliminar el Pokémon

        fun bind(pokemon: Pokemon) {
            pokemonNameTextView.text = pokemon.name
            pokemonType.text = "Tipos: ${pokemon.types.joinToString(", ")}"
            pokemonWeightTextView.text = "Weight: ${pokemon.weight} kg"
            pokemonHeightTextView.text = "Height: ${pokemon.height} m"

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

            // Detectar el clic en el botón de eliminar y llamar a onDeleteClick para eliminar el Pokémon
            deleteButton.setOnClickListener {
                onDeleteClick(pokemon)  // Llamamos al callback de eliminación
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



