package tarea3.perez_arias_cristian_pmdm_tarea_3.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import tarea3.perez_arias_cristian_pmdm_tarea_3.R
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.Pokemon

class DetallePokemonFragment : Fragment() {

    private lateinit var pokemon: Pokemon

    private lateinit var pokemonImageView: ImageView
    private lateinit var pokemonNameTextView: TextView
    private lateinit var pokemonTypeTextView: TextView
    private lateinit var pokemonWeightHeightTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pokemon = it.getParcelable(ARG_POKEMON) ?: Pokemon(
                id = 0,
                url = "",
                photoUrl = "",
                height = 0.0,
                weight = 0.0,
                name = "Desconocido",
                types = listOf("Desconocido")
            ) // Si no hay Pokémon en los argumentos, inicializamos uno por defecto
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detalle_pokemon, container, false)

        // Inicializa las vistas
        pokemonImageView = view.findViewById(R.id.pokemon_image_detalle)
        pokemonNameTextView = view.findViewById(R.id.pokemon_name_detalles)
        pokemonTypeTextView = view.findViewById(R.id.pokemon_tipo)
        pokemonWeightHeightTextView = view.findViewById(R.id.pokemon_peso_altura)

        // Setea los datos en las vistas
        pokemonNameTextView.text = pokemon.name

        // Muestra los tipos del Pokémon
        val types = pokemon.types.joinToString(", ") { it.capitalize() }
        pokemonTypeTextView.text = "Tipo(s): $types"

        // Muestra el peso y la altura
        pokemonWeightHeightTextView.text = "Peso: ${pokemon.weight} | Altura: ${pokemon.height}"

        // Carga la imagen usando Glide
        val imageUrl = pokemon.photoUrl ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
        Glide.with(view.context)
            .load(imageUrl)
            .into(pokemonImageView)

        return view
    }

    companion object {
        private const val ARG_POKEMON = "pokemon"

        @JvmStatic
        fun newInstance(pokemon: Pokemon) =
            DetallePokemonFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_POKEMON, pokemon)
                }
            }
    }
}
