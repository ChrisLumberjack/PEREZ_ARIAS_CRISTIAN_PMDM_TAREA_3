package tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tarea3.perez_arias_cristian_pmdm_tarea_3.R

// Adaptador para el RecyclerView que maneja la lista de Pokémon capturados
class PokemonAdapter(
    private val onItemClick: (Pokemon) -> Unit,       // Para manejar el clic en el Pokémon
    private val onDeleteClick: (Pokemon) -> Unit       // Para manejar el clic en el botón de eliminar
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    private var pokemons = mutableListOf<Pokemon>()    // Lista de Pokémon
    private var canDelete = false                       // Controla la visibilidad del botón de eliminar

    // Función para actualizar la lista de Pokémon en el adaptador
    fun submitList(newList: List<Pokemon>) {
        pokemons.clear()
        pokemons.addAll(newList)
        notifyDataSetChanged() // Actualiza el RecyclerView
    }

    // Función para configurar la lista de Pokémon directamente
    fun setPokemonList(pokemonList: List<Pokemon>) {
        this.pokemons = pokemonList.toMutableList()
        notifyDataSetChanged()
    }

    // Función para establecer si el botón de eliminar debe ser visible
    fun setCanDelete(canDelete: Boolean) {
        this.canDelete = canDelete
        notifyDataSetChanged() // Notificar que los datos han cambiado para reflejar el estado de "delete"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
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
        private val pokemonIndex: TextView = itemView.findViewById(R.id.pokemon_index)
        private val pokemonType: TextView = itemView.findViewById(R.id.pokemon_types)
        private val pokemonWeightTextView: TextView = itemView.findViewById(R.id.pokemon_weight)
        private val pokemonHeightTextView: TextView = itemView.findViewById(R.id.pokemon_height)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)

        // Esta función se llama para enlazar los datos del Pokémon con la vista
        fun bind(pokemon: Pokemon) {
            // Establecer los datos del Pokémon en las vistas correspondientes
            pokemonNameTextView.text = pokemon.name
            pokemonIndex.text = "Id: ${pokemon.id}"
            pokemonType.text = "Tipos: ${pokemon.types.joinToString(", ")}"
            pokemonWeightTextView.text = "Weight: ${pokemon.weight} kg"
            pokemonHeightTextView.text = "Height: ${pokemon.height} m"

            // Cargar la imagen usando Glide (si no hay URL se usa la URL por defecto de PokeAPI)
            val imageUrl = pokemon.photoUrl ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${getPokemonIndexFromUrl(pokemon.url)}.png"
            Glide.with(itemView.context)
                .load(imageUrl)  // Cargar la imagen desde la URL
                .into(pokemonImageView)  // Establecer la imagen en el ImageView

            // Establecer el clic en el ítem de la lista (para ver detalles o capturar el Pokémon)
            itemView.setOnClickListener {
                onItemClick(pokemon)  // Llamar al callback para manejar el clic en el Pokémon
            }

            // Configurar visibilidad del botón de eliminación (si se permite la eliminación)
            deleteButton.visibility = if (canDelete) View.VISIBLE else View.GONE

            // Establecer el clic en el botón de eliminar (para eliminar el Pokémon)
            deleteButton.setOnClickListener {
                onDeleteClick(pokemon)  // Llamar al callback para eliminar el Pokémon
            }
        }

        // Función para obtener el índice del Pokémon a partir de su URL
        private fun getPokemonIndexFromUrl(url: String): Int {
            val regex = Regex("pokemon/(\\d+)")
            val match = regex.find(url)
            return match?.groupValues?.get(1)?.toInt() ?: -1  // Devolver el índice o -1 si no se puede obtener
        }
    }
}
