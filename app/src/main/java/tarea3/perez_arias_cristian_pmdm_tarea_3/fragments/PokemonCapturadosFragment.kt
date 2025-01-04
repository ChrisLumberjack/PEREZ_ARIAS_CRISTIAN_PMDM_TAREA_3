package tarea3.perez_arias_cristian_pmdm_tarea_3.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import tarea3.perez_arias_cristian_pmdm_tarea_3.R
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.Pokemon
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.PokemonAdapter

class PokemonCapturadosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pokemonAdapter: PokemonAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Aquí puedes recuperar los parámetros si es necesario
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pokemon_capturados, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Crear el adaptador y pasarle la función onItemClick
        pokemonAdapter = PokemonAdapter(
            onItemClick = { pokemon ->
                // On click, navigate to the Detail Pokemon Fragment
                val detalleFragment = DetallePokemonFragment.newInstance(pokemon)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        detalleFragment
                    ) // Replace with the container ID
                    .addToBackStack(null) // Add to back stack so we can navigate back
                    .commit()
            },
            onDeleteClick = { pokemon ->
                // Call function to delete Pokémon
                deleteCapturedPokemon(pokemon)
            }
        )
        recyclerView.adapter = pokemonAdapter
        // Cargar los Pokémon capturados desde Firestore
        loadCapturedPokemons(requireContext())


        val button: Button = view.findViewById(R.id.btn_actualizar_pokedex)
        button.setOnClickListener() {
            loadCapturedPokemons(requireContext())
        }
        return view
    }

    // Función para cargar los Pokémon capturados de Firestore
    fun loadCapturedPokemons(requireContext: Context) {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val userEmail = user?.email ?: "unknown"

        // Accedemos a la colección de Pokémon capturados desde Firestore
        db.collection("pokemon")
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { result ->
                val capturedPokemons = mutableListOf<Pokemon>()
                for (document in result) {
                    val pokemon = document.toObject(Pokemon::class.java)

                    // Aseguramos que la URL de la imagen se obtenga correctamente desde Firestore
                    pokemon.photoUrl = document.getString("photoUrl")

                    // Aseguramos que los tipos se obtengan correctamente
                    val typesList = document.get("types") as? List<String>
                        ?: listOf()  // Obtenemos la lista de tipos
                    pokemon.types = typesList  // Asignamos la lista de tipos a nuestro Pokémon
                    val index = (document.get("index") as? Long)?.toInt() // Convirtiendo Long a Int si el valor es Long

                    // Si el valor es null, puedes manejar el caso según tu lógica
                    if (index != null) {
                        pokemon.id = index  // Asignando el valor de "index" al id de pokemon
                    } else {
                        // Manejo del caso cuando "index" no está presente o no es un valor válido
                        Log.e("Firestore", "El campo 'index' es nulo o no existe en el documento")
                    }
                    pokemonAdapter.setCanDelete(true)
                    capturedPokemons.add(pokemon)
                }

                // Actualizamos el adaptador con la lista de Pokémon capturados
                pokemonAdapter.submitList(capturedPokemons)

            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Error al cargar los Pokémon capturados",
                    Toast.LENGTH_SHORT
                ).show()
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
                    // If we find the Pokémon, delete it
                    val documentId = result.documents[0].id
                    db.collection("pokemon").document(documentId).delete()
                        .addOnSuccessListener {
                            Toast.makeText(context, "${pokemon.name} deleted!", Toast.LENGTH_SHORT)
                                .show()
                            // Refresh the list after deleting the Pokémon
                            loadCapturedPokemons(requireContext())
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                context,
                                "Error deleting the Pokémon",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error deleting the Pokémon", Toast.LENGTH_SHORT).show()
            }
    }


    companion object {
        @JvmStatic
        fun newInstance() = PokemonCapturadosFragment()
    }
}
