package tarea3.perez_arias_cristian_pmdm_tarea_3.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import tarea3.perez_arias_cristian_pmdm_tarea_3.R
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.Pokemon
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.PokemonAdapter
import tarea3.perez_arias_cristian_pmdm_tarea_3.model.SettingsViewModel

class PokemonCapturadosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        loadCapturedPokemons(requireContext())  // Llamar a la función para cargar los datos
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pokemon_capturados, container, false)
        val pokemonCapturadosFragment =
            parentFragmentManager.findFragmentByTag("PokemonCapturadosFragment") as? PokemonCapturadosFragment

// Luego llamas a la función de actualización
        pokemonCapturadosFragment?.loadCapturedPokemons(requireContext())
        // Inicialización del SettingsViewModel
        settingsViewModel = ViewModelProvider(requireActivity()).get(SettingsViewModel::class.java)

        // Obtener la instancia del ViewModel compartido
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        pokemonAdapter = PokemonAdapter(
            onItemClick = { pokemon ->
                val detalleFragment = DetallePokemonFragment.newInstance(pokemon)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, detalleFragment)
                    .addToBackStack(null)
                    .commit()
            },
            onDeleteClick = { pokemon ->
                deleteCapturedPokemon(pokemon)
            }
        )
        recyclerView.adapter = pokemonAdapter

        sharedViewModel.capturedPokemonsLiveData.observe(viewLifecycleOwner) { capturedPokemons ->
            // Actualiza la lista de Pokémon capturados
            pokemonAdapter.submitList(capturedPokemons)
        }

        loadCapturedPokemons(requireContext())

        val updatePokedex: Button = view.findViewById(R.id.btn_actualizar_pokedex)
        updatePokedex.isVisible = false
        updatePokedex.setOnClickListener {
            loadCapturedPokemons(requireContext())
        }

        // Observamos el cambio de visibilidad del botón eliminar
        settingsViewModel.deletePokemonEnabled.observe(viewLifecycleOwner) { canDelete ->
            pokemonAdapter.setCanDelete(canDelete)
        }

        return view
    }

    fun loadCapturedPokemons(requireContext: Context) {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val userEmail = user?.email ?: "unknown"

        db.collection("pokemon")
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { result ->
                val capturedPokemons = mutableListOf<Pokemon>()
                for (document in result) {
                    val pokemon = document.toObject(Pokemon::class.java)
                    pokemon.photoUrl = document.getString("photoUrl")
                    val typesList = document.get("types") as? List<String> ?: listOf()
                    pokemon.types = typesList
                    val index = (document.get("index") as? Long)?.toInt()
                    if (index != null) {
                        pokemon.id = index
                    } else {
                        Log.e("Firestore", "El campo 'index' es nulo o no existe en el documento")
                    }
                    capturedPokemons.add(pokemon)
                }
                pokemonAdapter.submitList(capturedPokemons)
            }
            .addOnFailureListener {
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
                    val documentId = result.documents[0].id
                    db.collection("pokemon").document(documentId).delete()
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "${pokemon.name} eliminado!",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadCapturedPokemons(requireContext())
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Error al eliminar el Pokémon",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al eliminar el Pokémon", Toast.LENGTH_SHORT).show()
            }
    }
}
