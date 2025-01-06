package com.keremkulac.journeylog.presentation.ui.vehicleView

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentVehicleViewBinding
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.HandleResult
import com.keremkulac.journeylog.util.SharedViewModel
import com.keremkulac.journeylog.util.SwipeToDeleteCallback
import com.keremkulac.journeylog.util.TranslationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class VehicleViewFragment :
    BaseFragment<FragmentVehicleViewBinding>(FragmentVehicleViewBinding::inflate) {

    @Inject
    lateinit var translationHelper: TranslationHelper
    private lateinit var sharedViewModel: SharedViewModel
    private val viewModel by viewModels<VehicleViewViewModel>()
    private lateinit var adapter: VehicleViewAdapter
    private var deletedVehicle: Vehicle? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        observeUser()
        observeAllVehicles()
        fabClick()
        createVehicleClick()
        observeDeleteVehicleResult()
        observeSaveVehicleResult()
    }

    @Suppress("UNCHECKED_CAST")
    private fun observeAllVehicles() {
        viewModel.getAllVehicles.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    val vehicleList = data as List<Vehicle>
                    setRecyclerView(vehicleList)
                    checkEmptyList(vehicleList)
                }, onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun observeDeleteVehicleResult() {
        viewModel.deleteVehicleResult.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    val snackbar = Snackbar.make(binding.root, data, Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.vehicle_delete_snackbar_title)) {
                            viewModel.saveVehicle(deletedVehicle!!)
                            val secondSnackbar = Snackbar.make(
                                binding.root, getString(R.string.vehicle_delete_snackbar_message),
                                Snackbar.LENGTH_LONG
                            )
                            secondSnackbar.show()
                        }
                    snackbar.show()
                }, onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun observeSaveVehicleResult() {
        viewModel.saveVehicleResult.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result, onSuccess = { data ->
                observeUser()
            }, onFailure = { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            })
        }
    }

    private fun observeUser() {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { user ->
            viewModel.getAllVehicles(user.id)
        }
    }

    private fun setRecyclerView(vehicleList: List<Vehicle>) {
        adapter = VehicleViewAdapter(translationHelper)
        binding.vehiclesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.vehicleList = ArrayList(vehicleList)
        binding.vehiclesRecyclerView.adapter = adapter
        clickVehicle(adapter)
        optionsMenu(vehicleList)
        swipeRecyclerViewItem(vehicleList)
    }

    private fun checkEmptyList(vehicleList: List<Vehicle>) {
        with(binding) {
            if (vehicleList.isEmpty()) {
                vehiclesRecyclerView.visibility = View.GONE
                fab.visibility = View.GONE
                vehicleCardView.visibility = View.GONE
                allVehicleTitle.visibility = View.GONE
                emptyWarning.visibility = View.VISIBLE
                createVehicle.visibility = View.VISIBLE
            } else {
                vehiclesRecyclerView.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
                vehicleCardView.visibility = View.VISIBLE
                allVehicleTitle.visibility = View.VISIBLE
                emptyWarning.visibility = View.GONE
                createVehicle.visibility = View.GONE
                vehicleCardInfo.text =
                    getString(R.string.vehicle_view_card_info_text).format(vehicleList.size)
                adapter.filterList(vehicleList as ArrayList<Vehicle>)
            }
        }
    }

    private fun optionsMenu(vehicleList: List<Vehicle>) {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.menu_search, menu)
                val search = menu.findItem(R.id.action_search)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (!newText.isNullOrEmpty()) {
                            checkEmptyList(viewModel.filter(newText, vehicleList))
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {

                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun navigateVehicleCreate() {
        findNavController().navigate(R.id.action_vehicleViewFragment_to_vehicleCreateFragment)
    }

    private fun fabClick() {
        binding.fab.setOnClickListener {
            navigateVehicleCreate()
        }
    }

    private fun createVehicleClick() {
        binding.createVehicle.setOnClickListener {
            navigateVehicleCreate()
        }
    }

    private fun clickVehicle(adapter: VehicleViewAdapter) {
        adapter.clickListener = { vehicle ->
            findNavController().navigate(
                VehicleViewFragmentDirections.actionVehicleViewFragmentToVehicleDetailFragment(
                    vehicle
                )
            )
        }
    }

    private fun swipeRecyclerViewItem(vehicleList: List<Vehicle>) {
        val swipeCallback = SwipeToDeleteCallback(adapter, requireContext()) { position ->
            deletedVehicle = vehicleList[position]
            viewModel.deleteVehicle(deletedVehicle!!.id)
            observeUser()
        }
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.vehiclesRecyclerView)
    }
}