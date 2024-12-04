package com.keremkulac.journeylog.presentation.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentProfileBinding
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.CustomDialog
import com.keremkulac.journeylog.util.HandleResult
import com.keremkulac.journeylog.util.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private lateinit var sharedViewModel: SharedViewModel
    private val viewModel by viewModels<ProfileViewModel>()
    private var user: User? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        signOut()
        observeSharedData()
        observeSignOutResult()
        passwordChange()
        navigateUpdateProfile()
        vehicleSettings()
    }

    private fun observeSharedData() {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { user ->
            setUserFields(user)
            observeGetProfilePictureUrlResult()
            this.user = user
        }
    }

    private fun passwordChange() {
        binding.passwordSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_updatePasswordFragment)
        }
    }

    private fun vehicleSettings() {
        binding.vehicleSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_vehicleSettingsFragment)
        }
    }

    private fun observeSignOutResult() {
        viewModel.signOutResult.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                }, onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun observeGetProfilePictureUrlResult() {
        viewModel.getProfilePictureUrlResult.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result, onSuccess = { url ->
                Glide.with(requireContext()).load(url).into(binding.profilePicture)
            })
        }
    }

    private fun setUserFields(user: User?) {
        user?.let {
            binding.userFullName.text = viewModel.formatFullName(it.name, it.surname)
            binding.userEmail.text = it.email
            if (it.imageUri != "") {
                viewModel.getProfilePictureUrl(it.imageUri)
            }
        }
    }

    private fun showDialog() {
        requireContext().apply {
            CustomDialog.showConfirmationDialog(
                this,
                getString(R.string.dialog_logout_title),
                getString(R.string.dialog_logout_message),
                getString(R.string.dialog_logout_positive_button_text),
                getString(R.string.dialog_logout_negative_button_text)
            ) {
                viewModel.signOut()
            }
        }

    }

    private fun signOut() {
        binding.logout.setOnClickListener {
            showDialog()
        }
    }

    private fun navigateUpdateProfile() {
        binding.profileEdit.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }
}