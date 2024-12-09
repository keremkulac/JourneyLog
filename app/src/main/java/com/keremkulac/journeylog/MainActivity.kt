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

    private fun setNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val navView: BottomNavigationView = binding.bottomNavigationView
        navView.itemIconTintList = null
        navView.setupWithNavController(navController)
    }

    private fun observeBottomNavigationVisibility() {
        viewModel.bottomNavVisibility.observe(this) { visibility ->
            binding.bottomNavigationView.visibility = visibility
            if (visibility == View.VISIBLE) {
                setToolbar()
            }
        }
    }

    private fun setBottomNavigationVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.bottomNavigationVisibility(destination.id)
            toolBarSet(destination.id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }


    private val toolbarSettings = mapOf(
        R.id.homeFragment to Pair("Ana Sayfa", false),
        R.id.profileFragment to Pair("Profil", true),
        R.id.editProfileFragment to Pair("Profil düzenle", true),
        R.id.forgotPasswordFragment to Pair("Şifremi unuttum", true),
        R.id.updatePasswordFragment to Pair("Şifre güncelle", true),
        R.id.fuelPurchaseViewFragment to Pair("Yakıt alımlarım", true),
        R.id.fuelPurchaseDetailFragment to Pair("Yakıt alımı detayı", true),
        R.id.addFuelPurchaseFragment to Pair("Yakıt alımı", true),
        R.id.vehicleViewFragment to Pair("Araçlarım", true),
        R.id.vehicleDetailFragment to Pair("Araç detayı", true),
        R.id.vehicleCreateFragment to Pair("Araç oluştur", true)
    )

    private fun toolBarSet(destinationId: Int) {
        val (title, hasBackButton) = toolbarSettings[destinationId] ?: return
        ToolbarUtil.setToolbar(this, binding.toolbar, title, true, hasBackButton)
    }
}