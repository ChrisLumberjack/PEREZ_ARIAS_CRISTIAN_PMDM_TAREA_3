package tarea3.perez_arias_cristian_pmdm_tarea_3

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import tarea3.perez_arias_cristian_pmdm_tarea_3.fragments.PokedexFragment
import tarea3.perez_arias_cristian_pmdm_tarea_3.fragments.PokemonCapturadosFragment
import tarea3.perez_arias_cristian_pmdm_tarea_3.fragments.AjustesFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 3 // Número de fragmentos
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PokemonCapturadosFragment() // Fragmento de Pokémon capturados
            1 -> PokedexFragment() // Fragmento de Pokedex
            2 -> AjustesFragment() // Fragmento de ajustes
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}

