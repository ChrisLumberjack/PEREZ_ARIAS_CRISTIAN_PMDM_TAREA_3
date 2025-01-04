package tarea3.perez_arias_cristian_pmdm_tarea_3.fragments

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import tarea3.perez_arias_cristian_pmdm_tarea_3.R
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.Pokemon
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.PokemonAdapter

class DetallePokemonFragment : Fragment() {

    private lateinit var pokemon: Pokemon
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var pokemonImageView: ImageView
    private lateinit var pokemonNameTextView: TextView
    private lateinit var pokemonTypeTextView: TextView
    private lateinit var pokemonWeightHeightTextView: TextView

    interface OnPokemonDeletedListener {
        fun onPokemonDeleted()
    }

    private var onPokemonDeletedListener: OnPokemonDeletedListener? = null

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
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detalle_pokemon, container, false)

        // Inicializa las vistas
        pokemonImageView = view.findViewById(R.id.pokemon_image_detalle)
        pokemonNameTextView = view.findViewById(R.id.pokemon_name_detalles)
        pokemonTypeTextView = view.findViewById(R.id.pokemon_tipo)
        pokemonWeightHeightTextView = view.findViewById(R.id.pokemon_peso_altura)

        // Setea los datos en las vistas
        pokemonNameTextView.text = pokemon.name
        val types = pokemon.types.joinToString(", ") { it.capitalize() }
        pokemonTypeTextView.text = "Tipo(s): $types"
        pokemonWeightHeightTextView.text = "Peso: ${pokemon.weight} | Altura: ${pokemon.height}"

        // Carga la imagen usando Glide
        val imageUrl = pokemon.photoUrl
            ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
        Glide.with(view.context)
            .load(imageUrl)
            .into(pokemonImageView)

        // Initialize the adapter
        pokemonAdapter = PokemonAdapter(
            onItemClick = { pokemon -> "" },
            onDeleteClick = { pokemon ->
                // Llamamos a la función para eliminar el Pokémon
                requireActivity().supportFragmentManager.popBackStack()

                onPokemonDeletedListener?.onPokemonDeleted()
                deleteCapturedPokemon(pokemon)
            }
        )

        val butonFinishDetails = view.findViewById<ImageButton>(R.id.finish_button)
        butonFinishDetails.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }


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

    private fun deleteCapturedPokemon(pokemon: Pokemon) {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val userEmail = user?.email ?: "unknown"

        db.collection("pokemon")
            .whereEqualTo("userEmail", userEmail)
            .whereEqualTo("name", pokemon.name)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val documentId = result.documents[0].id
                    db.collection("pokemon").document(documentId).delete()
                        .addOnSuccessListener {
                            if (isAdded) {
                                Toast.makeText(requireContext(), "${pokemon.name} deleted!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            if (isAdded) {
                                Toast.makeText(
                                    requireContext(),
                                    "Error deleting the Pokémon",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
            .addOnFailureListener {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Error deleting the Pokémon", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
