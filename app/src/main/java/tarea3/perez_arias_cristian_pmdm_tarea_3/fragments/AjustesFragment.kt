package tarea3.perez_arias_cristian_pmdm_tarea_3.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import tarea3.perez_arias_cristian_pmdm_tarea_3.LoginActivity
import tarea3.perez_arias_cristian_pmdm_tarea_3.R
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.Pokemon
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.PokemonAdapter

class AjustesFragment : Fragment() {

    private lateinit var switchDeletePokemon: Switch
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    val pokemonListLiveData: MutableLiveData<List<Pokemon>> = MutableLiveData()
    private val pokemonList = mutableListOf<Pokemon>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ajustes, container, false)

        // Inicializar SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Referencias a los views
        switchDeletePokemon = view.findViewById(R.id.switch_delete_pokemon)
        recyclerView = view.findViewById(R.id.recyclerView)

        // Configurar el RecyclerView y el adaptador
        recyclerView.layoutManager = LinearLayoutManager(context)
        pokemonAdapter = PokemonAdapter(
            onItemClick = { pokemon: Pokemon ->
                // Maneja el clic en el Pokémon
                Toast.makeText(context, "Clic en ${pokemon.name}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { pokemon: Pokemon ->
                // Maneja la eliminación del Pokémon
                pokemonList.remove(pokemon)
                pokemonAdapter.setPokemonList(pokemonList)
                Toast.makeText(context, "${pokemon.name} eliminado", Toast.LENGTH_SHORT).show()
            }
        )

        val closeSession: Button = view.findViewById(R.id.btn_logout)

        closeSession.setOnClickListener {
            closeSession()  // Llamas a tu función de cerrar sesión

            // Crear el Intent para iniciar la LoginActivity
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        recyclerView.adapter = pokemonAdapter
        pokemonAdapter.setPokemonList(pokemonList) // Establecer la lista de Pokémon

        // Cargar las preferencias guardadas
        loadSettings()

        // Configurar el Switch para la eliminación de Pokémon
        switchDeletePokemon.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "La eliminación de Pokémon está habilitada", Toast.LENGTH_SHORT).show()
                pokemonAdapter.setCanDelete(true)
                saveSetting("delete_pokemon", isChecked)
            } else {
                Toast.makeText(context, "La eliminación de Pokémon está deshabilitada", Toast.LENGTH_SHORT).show()
                pokemonAdapter.setCanDelete(false)
                saveSetting("delete_pokemon", isChecked)
            }

            // Llamar a loadCapturedPokemons desde el fragmento PokemonCapturadosFragment
            val pokemonCapturadosFragment = parentFragmentManager.findFragmentByTag(PokemonCapturadosFragment::class.java.simpleName) as? PokemonCapturadosFragment
            pokemonCapturadosFragment?.loadCapturedPokemons(requireContext())
        }

        return view
    }

    private fun loadSettings() {
        val deletePokemon = sharedPreferences.getBoolean("delete_pokemon", false)
        switchDeletePokemon.isChecked = deletePokemon
        pokemonAdapter.setCanDelete(deletePokemon)
    }

    private fun saveSetting(key: String, value: Any) {
        val editor = sharedPreferences.edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is String -> editor.putString(key, value)
        }
        editor.apply()
    }

    private fun closeSession() {
        Firebase.auth.signOut()
    }
}
