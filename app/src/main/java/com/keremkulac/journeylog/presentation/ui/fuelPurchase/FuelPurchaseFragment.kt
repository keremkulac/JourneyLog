package com.keremkulac.journeylog.presentation.ui.fuelPurchase


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.databinding.FragmentFuelPurchaseBinding
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FuelPurchaseFragment :
    BaseFragment<FragmentFuelPurchaseBinding>(FragmentFuelPurchaseBinding::inflate) {

    private val viewModel by viewModels<FuelPurchaseViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateAddFuelPurchase()
        observeAllReceipts()
    }

    private fun navigateAddFuelPurchase() {
        binding.fab.setOnClickListener {
            findNavController().navigate(
                R.id.action_fuelPurchaseFragment_to_addFuelPurchaseFragment,
                null,
                navOptions {
                    popUpTo(R.id.fuelPurchaseFragment) { inclusive = true }
                })
        }
    }

    private fun observeAllReceipts() {
        viewModel.allReceipts.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    createRecyclerView(result.data as List<Receipt>)
                }
            }
        }
    }

    private fun createRecyclerView(list: List<Receipt>) {
        val adapter = FuelPurchaseAdapter()
        binding.fuelPurchaseRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.receiptList = list as ArrayList<Receipt>
        binding.fuelPurchaseRecyclerView.adapter = adapter
    }
}