package tarea3.perez_arias_cristian_pmdm_tarea_3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Configura el ViewPager2 con un adaptador
        val pagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        // Vincula el TabLayout con el ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Puedes asignar títulos a las pestañas dependiendo de la posición
            when (position) {
                0 -> tab.text = "Pokémon Capturados"
                1 -> tab.text = "Pokédex"
                2 -> tab.text = "Ajustes"
            }
        }.attach()

        // Configura el BottomNavigationView si lo deseas

    }
}
