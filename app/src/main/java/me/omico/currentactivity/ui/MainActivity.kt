package me.omico.currentactivity.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import me.omico.core.fragment.fragment
import me.omico.core.viewbinding.viewBinding
import me.omico.currentactivity.R
import me.omico.currentactivity.databinding.MainActivityBinding

/**
 * @author Omico 2020/12/15
 */
class MainActivity : AppCompatActivity(R.layout.main_activity) {

    private val binding: MainActivityBinding by viewBinding()
    private val navHostFragment: NavHostFragment by fragment(R.id.navigation_host_fragment)

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        navController = navHostFragment.navController.also {
            it.setGraph(R.navigation.app)
            setupActionBarWithNavController(it)
        }
    }
}
