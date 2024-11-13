package com.keremkulac.journeylog.presentation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.databinding.FragmentHomeBinding
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressCancel()
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            when (currentUser) {
                is Result.Success -> getUser(currentUser.data!!.uid)
                else -> {}
            }
        }
    }

    private fun getUser(id: String) {
        viewModel.getUser(id)
        viewModel.userResult.observe(viewLifecycleOwner) { userResult ->
            when (userResult) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> binding.progressBar.visibility = View.GONE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("TAG", userResult.data.toString())
                }
            }
        }
    }

    private fun onBackPressCancel() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            })
    }

}