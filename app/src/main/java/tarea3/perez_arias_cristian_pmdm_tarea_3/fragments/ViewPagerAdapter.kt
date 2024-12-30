package tarea3.perez_arias_cristian_pmdm_tarea_3

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import tarea3.perez_arias_cristian_pmdm_tarea_3.fragments.PokedexFragment
import tarea3.perez_arias_cristian_pmdm_tarea_3.fragments.PokemonCapturadosFragment
import tarea3.perez_arias_cristian_pmdm_tarea_3.fragments.AjustesFragment

class ViewPagerAdapter(activity: MainActivity) : FragmentStateAdapter(activity) {

    // Devuelve el fragmento correspondiente a la posición
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PokemonCapturadosFragment()  // Fragment para Pokémon Capturados
            1 -> PokedexFragment()          // Fragment para Pokédex
            else -> AjustesFragment()      // Fragment para Ajustes
        }
    }

    // Devuelve el número total de páginas
    override fun getItemCount(): Int = 3
}
