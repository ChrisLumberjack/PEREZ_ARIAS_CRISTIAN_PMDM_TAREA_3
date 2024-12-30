package tarea3.perez_arias_cristian_pmdm_tarea_3.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pokemon_capturados, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Crear el adaptador y pasarle la función onItemClick
        pokemonAdapter = PokemonAdapter(
            onItemClick = { pokemon ->
                // Acción que se ejecuta al hacer clic en un Pokémon
                Toast.makeText(context, "Pokémon seleccionado: ${pokemon.name}", Toast.LENGTH_SHORT).show()
                // Aquí puedes lanzar una nueva actividad o fragmento con los detalles del Pokémon
            }
        )
        recyclerView.adapter = pokemonAdapter

        // Cargar los Pokémon capturados desde Firestore
        loadCapturedPokemons()





        val button: Button = view.findViewById(R.id.btn_actualizar_pokedex)
        button.setOnClickListener(){
            loadCapturedPokemons()
        }
        return view
    }

    // Función para cargar los Pokémon capturados de Firestore
    fun loadCapturedPokemons() {
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
                    // Aseguramos que se obtiene correctamente la URL de la imagen desde Firestore
                    pokemon.photoUrl = document.getString("photoUrl")  // Aquí añadimos la URL de Firestore
                    capturedPokemons.add(pokemon)
                }

                // Actualizamos el adaptador con la lista de Pokémon capturados
                pokemonAdapter.submitList(capturedPokemons)

            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error al cargar los Pokémon capturados", Toast.LENGTH_SHORT).show()
            }
    }




    companion object {
        @JvmStatic
        fun newInstance() = PokemonCapturadosFragment()
    }
}
