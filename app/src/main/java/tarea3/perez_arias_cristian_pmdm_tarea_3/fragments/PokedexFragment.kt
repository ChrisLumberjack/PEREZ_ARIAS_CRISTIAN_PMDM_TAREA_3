package tarea3.perez_arias_cristian_pmdm_tarea_3.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tarea3.perez_arias_cristian_pmdm_tarea_3.R
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.CapturedPokemon
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.Pokemon
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.PokemonAdapter
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.PokemonDetails
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
        pokemonAdapter = PokemonAdapter(
            onItemClick = { pokemon ->
                capturePokemon(pokemon)
            },
            onDeleteClick = { pokemon ->
                // Llamamos a la función para eliminar el Pokémon
                ""
            }
        )



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

                    // Crear una lista mutable para guardar los Pokémon con los detalles completos
                    val pokemonDetailsList = mutableListOf<Pokemon>()
                    var errorOccurred = false // Control para verificar si hubo un error

                    pokemonList.forEach { pokemon ->
                        val pokemonIndex = getPokemonIndexFromUrl(pokemon.url)
                        pokemonService.getPokemonDetails(pokemonIndex)
                            .enqueue(object : Callback<PokemonDetails> {
                                override fun onResponse(
                                    call: Call<PokemonDetails>,
                                    response: Response<PokemonDetails>,
                                ) {
                                    if (response.isSuccessful) {
                                        val pokemonDetails = response.body()
                                        pokemonDetails?.let {
                                            // Aquí obtenemos los detalles y los agregamos a la lista
                                            val updatedPokemon = Pokemon(
                                                id = it.id,
                                                name = pokemon.name,
                                                url = pokemon.url,
                                                photoUrl = it.sprites.front_default,
                                                types = it.types.map { type -> type.type.name },  // Accedemos directamente a `type.name`
                                                weight = it.weight.toDouble(),  // Convertimos a Double
                                                height = it.height.toDouble()   // Convertimos a Double
                                            )

                                            // Agregar el Pokémon actualizado a la lista
                                            pokemonDetailsList.add(updatedPokemon)

                                            // Si hemos terminado de procesar todos los Pokémon, actualizamos el adaptador
                                            if (pokemonDetailsList.size == pokemonList.size) {
                                                pokemonAdapter.submitList(pokemonDetailsList)
                                                pokemonAdapter.setCanDelete(false)

                                                // Mostrar un solo mensaje Toast después de completar el proceso
                                                if (!errorOccurred) {
                                                    Toast.makeText(
                                                        context,
                                                        "Lista de Pokémon cargada correctamente",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    } else {
                                        errorOccurred = true
                                        // En caso de error en la carga de detalles, mostrar solo un mensaje
                                        Toast.makeText(
                                            context,
                                            "Error al cargar detalles de Pokémon",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }

                                override fun onFailure(call: Call<PokemonDetails>, t: Throwable) {
                                    errorOccurred = true
                                    // En caso de fallo en la conexión
                                    Toast.makeText(
                                        context,
                                        "Fallo en la conexión al obtener detalles",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    }
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

    object RetrofitInstance {
        private const val BASE_URL = "https://pokeapi.co/api/v2/"

        val pokemonService: PokemonService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PokemonService::class.java)
        }
    }

    // Esta función maneja la captura de un Pokémon
    private fun capturePokemon(pokemon: Pokemon) {
        // Obtén el usuario actual desde Firebase Authentication
        val user = FirebaseAuth.getInstance().currentUser
        val userEmail =
            user?.email ?: "unknown" // Si el usuario no está autenticado, ponemos "unknown"

        // Creamos la instancia de Retrofit para obtener los detalles del Pokémon
        val pokemonService = RetrofitInstance.pokemonService

        // Obtenemos el índice del Pokémon de la URL y lo usamos directamente en la llamada a Retrofit
        val pokemonIndex = getPokemonIndexFromUrl(pokemon.url)

        // Realizamos la solicitud para obtener los detalles del Pokémon
        pokemonService.getPokemonDetails(pokemonIndex).enqueue(object : Callback<PokemonDetails> {
            override fun onResponse(
                call: Call<PokemonDetails>,
                response: Response<PokemonDetails>,
            ) {
                if (response.isSuccessful) {
                    // Obtenemos los detalles del Pokémon
                    val pokemonDetails = response.body()

                    // Si el Pokémon se encuentra, continuamos con el proceso
                    if (pokemonDetails != null) {
                        val capturedPokemon = CapturedPokemon(
                            name = pokemon.name,
                            index = pokemonIndex,
                            photoUrl = pokemon.photoUrl
                                ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemonIndex}.png",
                            types = pokemonDetails.types.map { it.type.name },
                            weight = pokemonDetails.weight,
                            height = pokemonDetails.height,
                            userEmail = userEmail
                        )

                        // Guardamos el Pokémon en Firestore
                        val db = FirebaseFirestore.getInstance()
                        val pokemonRef = db.collection("pokemon")

                        // Verificamos si el Pokémon ya ha sido capturado
                        pokemonRef
                            .whereEqualTo(
                                "userEmail",
                                userEmail
                            )  // Filtramos por el correo del usuario
                            .whereEqualTo(
                                "name",
                                pokemon.name
                            )  // Filtramos por el nombre del Pokémon
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
                } else {
                    Toast.makeText(
                        context,
                        "Error al obtener los detalles del Pokémon",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<PokemonDetails>, t: Throwable) {
                Toast.makeText(context, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


    // Función para obtener el índice del Pokémon a partir de la URL
    fun getPokemonIndexFromUrl(url: String): Int {
        val regex = Regex("""pokemon/(\d+)/""")
        val matchResult = regex.find(url)
        return matchResult?.groups?.get(1)?.value?.toInt() ?: 0
    }


}
