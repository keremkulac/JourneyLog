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
import com.keremkulac.journeylog.util.ToolbarUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        binding.toolbar.title = ""
        setContentView(R.layout.activity_main)
        setContentView(binding.root)
        setNavigationView()
        setBottomNavigationVisibility()
        setToolbar()
    }

    private fun setNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val navView: BottomNavigationView = binding.bottomNavigationView
        navController.setGraph(R.navigation.nav_graph)
        navView.itemIconTintList = null
        navView.setupWithNavController(navController)
    }

    private fun setBottomNavigationVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                in hideBottomNavIds -> setVisibility(
                    bottomNavVisible = false,
                    toolbarVisible = false
                )

                in hideBottomNavShowToolBarIds -> setVisibility(
                    bottomNavVisible = false,
                    toolbarVisible = true
                )

                R.id.homeFragment -> setVisibility(
                    bottomNavVisible = true,
                    toolbarVisible = false
                )

                else -> {
                    toolBarSet(destination.id)
                    setVisibility(bottomNavVisible = true, toolbarVisible = true)
                }
            }
        }
    }

    private val hideBottomNavIds = setOf(
        R.id.loginFragment,
        R.id.signupFragment,
        R.id.forgotPasswordFragment
    )

    private val hideBottomNavShowToolBarIds = setOf(
        R.id.vehicleDetailFragment,
        R.id.fuelPurchaseDetailFragment,
        R.id.fuelPurchaseAddFragment,
        R.id.vehicleCreateFragment
    )

    private fun setVisibility(bottomNavVisible: Boolean, toolbarVisible: Boolean) {
        binding.bottomNavigationView.visibility = if (bottomNavVisible) View.VISIBLE else View.GONE
        binding.toolbar.visibility = if (toolbarVisible) View.VISIBLE else View.GONE
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private val toolbarSettings = mapOf(
        R.id.profileFragment to Pair("Profil", true),
        R.id.editProfileFragment to Pair("Profil düzenle", true),
        R.id.forgotPasswordFragment to Pair("Şifremi unuttum", true),
        R.id.updatePasswordFragment to Pair("Şifre güncelle", true),
        R.id.fuelPurchaseViewFragment to Pair("Yakıt alımlarım", true),
        R.id.fuelPurchaseDetailFragment to Pair("Yakıt alımı detayı", true),
        R.id.fuelPurchaseAddFragment to Pair("Yakıt alımı", true),
        R.id.vehicleViewFragment to Pair("Araçlarım", true),
        R.id.vehicleDetailFragment to Pair("Araç detayı", true),
        R.id.vehicleCreateFragment to Pair("Araç oluştur", true),
        R.id.appSettingsFragment to Pair("Ayarlar", true)
    )

    private fun toolBarSet(destinationId: Int) {
        val (title, hasBackButton) = toolbarSettings[destinationId] ?: return
        ToolbarUtil.setToolbar(this, true, hasBackButton)
        binding.toolbarTitle.text = title
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}