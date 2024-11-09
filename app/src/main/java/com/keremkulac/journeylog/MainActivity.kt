package com.keremkulac.journeylog

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.keremkulac.journeylog.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)
        setNavigationView()
        observeBottomNavigationVisibility()
        setBottomNavigationVisibility()
    }

    private fun setNavigationView(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val navView: BottomNavigationView = binding.bottomNavigationView
        navView.itemIconTintList = null
        navView.setupWithNavController(navController)
    }

    private fun observeBottomNavigationVisibility() {
        viewModel.bottomNavVisibility.observe(this) { visibility ->
            binding.bottomNavigationView.visibility = visibility
        }
    }

    private fun setBottomNavigationVisibility(){
        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.bottomNavigationVisibility(destination.id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}