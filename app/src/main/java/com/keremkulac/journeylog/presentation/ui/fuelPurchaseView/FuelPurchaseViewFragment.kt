package com.keremkulac.journeylog.presentation.ui.fuelPurchaseView


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.databinding.FragmentFuelPurchaseViewBinding
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.util.HandleResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FuelPurchaseViewFragment :
    BaseFragment<FragmentFuelPurchaseViewBinding>(FragmentFuelPurchaseViewBinding::inflate) {

    private val viewModel by viewModels<FuelPurchaseViewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateAddFuelPurchase()
        observeAllReceipts()
    }

    private fun navigateAddFuelPurchase() {
        binding.fab.setOnClickListener {
            findNavController().navigate(
                R.id.action_fuelPurchaseViewFragment_to_addFuelPurchaseFragment,
                null,
                navOptions {
                    popUpTo(R.id.fuelPurchaseViewFragment) { inclusive = true }
                }
            )
        }
    }

    private fun observeAllReceipts() {
        viewModel.allReceipts.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result, onSuccess = { data ->
                createRecyclerView(data as List<Receipt>)
            })
        }
    }

    private fun createRecyclerView(list: List<Receipt>) {
        val adapter = FuelPurchaseViewAdapter()
        binding.fuelPurchaseRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.receiptList = list as ArrayList<Receipt>
        binding.fuelPurchaseRecyclerView.adapter = adapter
        clickListener(adapter)
    }


    private fun clickListener(adapter: FuelPurchaseViewAdapter) {
        adapter.clickListener = { receipt ->
            findNavController().navigate(
                FuelPurchaseViewFragmentDirections.actionFuelPurchaseViewFragmentToFuelPurchaseDetailFragment(
                    receipt
                )
            )
        }
    }
}