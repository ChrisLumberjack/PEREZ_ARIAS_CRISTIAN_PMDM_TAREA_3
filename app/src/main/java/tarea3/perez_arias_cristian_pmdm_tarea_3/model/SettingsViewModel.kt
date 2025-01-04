package tarea3.perez_arias_cristian_pmdm_tarea_3.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private val _deletePokemonEnabled = MutableLiveData<Boolean>()
    val deletePokemonEnabled: LiveData<Boolean> = _deletePokemonEnabled

    fun setDeletePokemonEnabled(enabled: Boolean) {
        _deletePokemonEnabled.value = enabled
    }
}
