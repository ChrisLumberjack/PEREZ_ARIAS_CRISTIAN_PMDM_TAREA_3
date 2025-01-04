package tarea3.perez_arias_cristian_pmdm_tarea_3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import tarea3.perez_arias_cristian_pmdm_tarea_3.fragments.AjustesFragment
import tarea3.perez_arias_cristian_pmdm_tarea_3.fragments.PokedexFragment
import tarea3.perez_arias_cristian_pmdm_tarea_3.fragments.PokemonCapturadosFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar el ViewPager2
        viewPager2 = findViewById(R.id.view_pager)

        // Configurar el adaptador para ViewPager2
        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 3 // Tres fragmentos
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> PokemonCapturadosFragment()  // Pestaña Pokémon Capturados
                    1 -> PokedexFragment()  // Pestaña Pokédex
                    else -> AjustesFragment()  // Pestaña Ajustes
                }
            }
        }

        viewPager2.adapter = adapter

        // Inicializar el BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Sincronizar el BottomNavigationView con el ViewPager2
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_pokedex -> viewPager2.currentItem = 1 // Cambiar a la página 1 (Pokédex)
                R.id.nav_pokemon_capturados -> viewPager2.currentItem = 0 // Cambiar a la página 0 (Capturados)
                R.id.nav_ajustes -> viewPager2.currentItem = 2 // Cambiar a la página 2 (Ajustes)
            }
            true
        }

        // Sincronizar el ViewPager2 con el BottomNavigationView
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> bottomNavigationView.selectedItemId = R.id.nav_pokemon_capturados
                    1 -> bottomNavigationView.selectedItemId = R.id.nav_pokedex
                    2 -> bottomNavigationView.selectedItemId = R.id.nav_ajustes
                }
            }
        })

        // Configurar el TabLayout para que funcione con ViewPager2
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Pokémon Capturados"
                1 -> tab.text = "Pokédex"
                2 -> tab.text = "Ajustes"
            }
        }.attach()
    }
}
