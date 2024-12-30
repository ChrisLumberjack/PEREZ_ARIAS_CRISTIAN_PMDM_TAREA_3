package tarea3.perez_arias_cristian_pmdm_tarea_3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias a TabLayout y ViewPager2
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)

        // Configurar el adaptador para ViewPager2
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Configurar el TabLayout para que se sincronice con el ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Pokémon Capturados"
                1 -> tab.text = "Pokédex"
                2 -> tab.text = "Ajustes"
            }
        }.attach()
    }
}
