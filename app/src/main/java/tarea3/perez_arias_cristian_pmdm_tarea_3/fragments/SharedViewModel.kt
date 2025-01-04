package tarea3.perez_arias_cristian_pmdm_tarea_3.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tarea3.perez_arias_cristian_pmdm_tarea_3.pokedex.Pokemon

class SharedViewModel : ViewModel() {
    private val _capturedPokemonsLiveData = MutableLiveData<List<Pokemon>>()
    val capturedPokemonsLiveData: LiveData<List<Pokemon>> = _capturedPokemonsLiveData

    fun updateCapturedPokemons(pokemonList: List<Pokemon>) {
        _capturedPokemonsLiveData.postValue(pokemonList)
    }
}