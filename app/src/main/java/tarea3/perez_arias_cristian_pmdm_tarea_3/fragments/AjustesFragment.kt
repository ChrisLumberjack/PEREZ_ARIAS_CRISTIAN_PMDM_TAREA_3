package tarea3.perez_arias_cristian_pmdm_tarea_3.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import tarea3.perez_arias_cristian_pmdm_tarea_3.R
import tarea3.perez_arias_cristian_pmdm_tarea_3.model.SettingsViewModel

class AjustesFragment : Fragment() {

    private lateinit var switchDeletePokemon: Switch
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ajustes, container, false)

        settingsViewModel = ViewModelProvider(requireActivity()).get(SettingsViewModel::class.java)

        switchDeletePokemon = view.findViewById(R.id.switch_delete_pokemon)

        // Cargar el valor actual del SharedPreferences
        loadSettings()

        switchDeletePokemon.setOnCheckedChangeListener { _, isChecked ->
            // Actualizar el ViewModel con el valor del switch
            settingsViewModel.setDeletePokemonEnabled(isChecked)

            // Guardar el valor en SharedPreferences
            saveSetting("delete_pokemon", isChecked)
        }

        return view
    }

    private fun loadSettings() {
        val deletePokemon = sharedPreferences.getBoolean("delete_pokemon", false)
        switchDeletePokemon.isChecked = deletePokemon
    }

    private fun saveSetting(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
}
