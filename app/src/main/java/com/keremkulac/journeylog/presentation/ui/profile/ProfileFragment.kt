package com.keremkulac.journeylog.presentation.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentProfileBinding
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.Result
import com.keremkulac.journeylog.util.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private lateinit var sharedViewModel: SharedViewModel
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        signOut()
        observeSharedData()
        observeSignOutResult()
        passwordChange()
    }

    private fun observeSharedData() {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { user ->
            Log.d("TAG1", user.toString())

        }
    }

    private fun passwordChange() {
        binding.passwordSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_updatePasswordFragment)
        }
    }

    private fun signOut() {
        binding.logout.setOnClickListener {
            viewModel.signOut()
        }
    }

    private fun observeSignOutResult() {
        viewModel.signOutResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.data, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                }
            }
        }
    }

}