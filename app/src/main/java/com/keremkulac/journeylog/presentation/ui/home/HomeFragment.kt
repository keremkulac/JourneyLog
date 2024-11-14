package com.keremkulac.journeylog.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.databinding.FragmentHomeBinding
import com.keremkulac.journeylog.domain.model.User
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
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> binding.progressBar.visibility = View.GONE
                is Result.Success -> {
                    viewModel.getUser(currentUser.data!!.uid)
                    getUser()
                }
            }
        }
    }

    private fun getUser() {
        viewModel.userResult.observe(viewLifecycleOwner) { userResult ->
            when (userResult) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> binding.progressBar.visibility = View.GONE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val user = userResult.data as User
                    binding.initials.text = viewModel.formatInitials(user.name, user.surname)
                    binding.userFullName.text = viewModel.formatFullName(user.name, user.surname)
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