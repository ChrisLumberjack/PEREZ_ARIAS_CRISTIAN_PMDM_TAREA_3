package tarea3.perez_arias_cristian_pmdm_tarea_3.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout.Tab
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tarea3.perez_arias_cristian_pmdm_tarea_3.R
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.Pokemon
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.PokemonAdapter
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.PokemonDetailResponse
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.PokemonResponse
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.PokemonService

class PokedexFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var retrofit: Retrofit
    private lateinit var pokemonService: PokemonService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Retrofit
        retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        pokemonService = retrofit.create(PokemonService::class.java)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pokedex, container, false)

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // Define el comportamiento de onItemClick
        pokemonAdapter = PokemonAdapter { pokemon ->
            capturePokemon(pokemon) // Aquí puedes manejar el clic en un Pokémon
        }

        recyclerView.adapter = pokemonAdapter

        // Llamar al servicio para obtener la lista de Pokémon
        loadPokemonList()

        return view
    }

    private fun loadPokemonList() {
        pokemonService.getPokemonList().enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(
                call: Call<PokemonResponse>,
                response: Response<PokemonResponse>,
            ) {
                if (response.isSuccessful) {
                    val pokemonList = response.body()?.results ?: emptyList()
                    pokemonAdapter.submitList(pokemonList) // Actualiza la lista en el adaptador
                } else {
                    Toast.makeText(
                        context,
                        "Error al cargar la lista de Pokémon",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                Toast.makeText(context, "Fallo en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Esta función maneja la captura de un Pokémon
    private fun capturePokemon(pokemon: Pokemon) {
        // Obtén el usuario actual desde Firebase Authentication
        val user = FirebaseAuth.getInstance().currentUser
        val userEmail = user?.email ?: "unknown" // Si el usuario no está autenticado, ponemos "unknown"
        data class CapturedPokemon(
            val name: String,
            val index: Int,
            val photoUrl: String,
            val types: List<String>,
            val weight: Int,
            val height: Int,
            val userEmail: String,
        )
        // Creamos un objeto CapturedPokemon
        val capturedPokemon = CapturedPokemon(
            name = pokemon.name,
            index = getPokemonIndexFromUrl(pokemon.url),
            photoUrl = pokemon.photoUrl ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${getPokemonIndexFromUrl(pokemon.url)}.png",
            types = listOf("Normal"), // Aquí puedes obtener los tipos reales
            weight = 0, // Aquí puedes obtener el peso real
            height = 0, // Aquí puedes obtener la altura real
            userEmail = userEmail
        )

        // Guardamos el Pokémon en Firestore
        val db = FirebaseFirestore.getInstance()
        val pokemonRef = db.collection("pokemon")

        // Verificamos si el Pokémon ya ha sido capturado
        pokemonRef
            .whereEqualTo("userEmail", userEmail)  // Filtramos por el correo del usuario
            .whereEqualTo("name", pokemon.name)  // Filtramos por el nombre del Pokémon
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    // Si el Pokémon no ha sido capturado, lo agregamos a la base de datos
                    pokemonRef.document(pokemon.name).set(capturedPokemon)
                        .addOnSuccessListener {
                            // Aquí se asegura que el Pokémon se haya capturado correctamente
                            Toast.makeText(
                                context,
                                "${pokemon.name} capturado!",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Llamamos a loadCapturedPokemons() para refrescar la lista
                        }
                        .addOnFailureListener { exception ->
                            // Si ocurre un error al escribir el Pokémon en la base de datos
                            Toast.makeText(
                                context,
                                "Error al capturar el Pokémon: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    // Si el Pokémon ya ha sido capturado, mostramos un mensaje
                    Toast.makeText(
                        context,
                        "¡Ya has capturado a ${pokemon.name}!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                // Aquí manejamos el error si ocurre al verificar la captura
                Toast.makeText(
                    context,
                    "Error al verificar la captura: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // Función para obtener el índice del Pokémon a partir de la URL
    fun getPokemonIndexFromUrl(url: String): Int {
        val regex = Regex("""pokemon/(\d+)/""")
        val matchResult = regex.find(url)
        return matchResult?.groups?.get(1)?.value?.toInt() ?: 0
    }

    // Función para obtener los tipos del Pokémon a partir de la URL (puedes usar Retrofit para hacer una petición HTTP)
    fun getPokemonTypes(url: String): List<String> {
        // Aquí puedes realizar una petición a la API de PokeAPI para obtener los tipos de un Pokémon usando su URL
        // Esto es solo un ejemplo simple. Asegúrate de hacer la petición correctamente en tu código.
        // Ejemplo de URL: https://pokeapi.co/api/v2/pokemon/{id}

        // Aquí puedes usar Retrofit o alguna otra librería para obtener esta información.
        // Para simplificar, solo devolveremos una lista de ejemplo:
        return listOf("Normal", "Flying")  // Ejemplo de tipos
    }
}
