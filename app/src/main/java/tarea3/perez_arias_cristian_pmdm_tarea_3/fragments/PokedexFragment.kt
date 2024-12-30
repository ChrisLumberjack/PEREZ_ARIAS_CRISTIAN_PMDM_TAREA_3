package tarea3.perez_arias_cristian_pmdm_tarea_3.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflar el layout del fragment
        val view = inflater.inflate(R.layout.fragment_pokedex, container, false)

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        pokemonAdapter = PokemonAdapter()  // No pasamos lista aquí, la actualizamos después
        recyclerView.adapter = pokemonAdapter

        // Llamar al servicio para obtener la lista de Pokémon
        loadPokemonList()

        return view
    }

    private fun loadPokemonList() {
        pokemonService.getPokemonList().enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    val pokemonList = response.body()?.results ?: emptyList()
                    // Actualizamos la lista en el adaptador
                    pokemonAdapter.submitList(pokemonList)
                } else {
                    Toast.makeText(context, "Error al cargar la lista de Pokémon", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                Toast.makeText(context, "Fallo en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun capturePokemon(pokemon: Pokemon) {
        // Hacer la petición para obtener los detalles del Pokémon
        pokemonService.getPokemonDetails(pokemon.url).enqueue(object :
            Callback<PokemonDetailResponse> {
            override fun onResponse(call: Call<PokemonDetailResponse>, response: Response<PokemonDetailResponse>) {
                if (response.isSuccessful) {
                    val pokemonDetails = response.body()
                    pokemonDetails?.let {
                        // Guardar en Firebase
                        val pokemonRef = FirebaseDatabase.getInstance().getReference("captured_pokemon")
                        pokemonRef.child(it.name).setValue(it)
                        Toast.makeText(context, "${it.name} capturado!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error al obtener los detalles del Pokémon", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PokemonDetailResponse>, t: Throwable) {
                Toast.makeText(context, "Fallo en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
