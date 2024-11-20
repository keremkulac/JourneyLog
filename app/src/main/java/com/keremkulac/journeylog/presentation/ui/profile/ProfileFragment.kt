package com.keremkulac.journeylog.presentation.ui.profile

import android.os.Bundle
import android.util.Log
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
import com.keremkulac.journeylog.util.GalleryPermissionManager
import com.keremkulac.journeylog.util.Result
import com.keremkulac.journeylog.util.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private lateinit var sharedViewModel: SharedViewModel
    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var galleryPermissionManager: GalleryPermissionManager
    private var user: User? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        signOut()
        binding.progressBar.bringToFront()
        galleryPermissionManager = GalleryPermissionManager(this) { uri ->
            binding.profilePicture.setImageURI(uri)
            val path = viewModel.createUUID() + ".jpeg"
            viewModel.saveProfilePicture(uri, path)
            user?.let {
                it.imageUri = path
                viewModel.updateUser(it)
            }
        }
        observeSharedData()
        observeSignOutResult()
        passwordChange()
        selectPicture()
        observeSaveProfilePictureResult()
        observeGetProfilePictureUrlResult()
        navigateUpdateProfile()
    }

    private fun observeSharedData() {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { user ->
            this.user = user
            Log.d("TAG12", user.toString())
            setUserFields()
            if (user.imageUri.isNotEmpty()) {
                viewModel.getProfilePictureUrl(user.imageUri)
            }
        }
    }

    private fun passwordChange() {
        binding.passwordSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_updatePasswordFragment)
        }
    }

    private fun signOut() {
        binding.logout.setOnClickListener {
            showDialog()
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

    private fun observeSaveProfilePictureResult() {
        viewModel.saveProfilePictureResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun observeGetProfilePictureUrlResult() {
        viewModel.getProfilePictureUrlResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Glide.with(requireContext()).load(result.data).into(binding.profilePicture)
                }

                else -> {}
            }
        }
    }

    private fun showDialog() {
        CustomDialog.showConfirmationDialog(
            requireContext(),
            "Çıkış yap",
            "Hesabınızdan çıkış yapmak istediğinize emin misiniz?",
            "Çıkış yap",
            "İptal"
        ) {
            viewModel.signOut()
        }
    }

    private fun selectPicture() {
        binding.profilePicture.setOnClickListener {
            galleryPermissionManager.checkGalleryPermission()
        }
    }


    private fun setUserFields() {
        val updatedUser = arguments?.getParcelable<User>("user")
        if (updatedUser != null) {
            user = updatedUser
        }
        user?.let {
            if (it.imageUri != "") {
                Glide.with(requireContext()).load(it.imageUri).into(binding.profilePicture)
            }
            binding.userFullName.text = viewModel.formatFullName(it.name, it.surname)
            binding.userEmail.text = it.email
        }
    }

    private fun navigateUpdateProfile() {
        binding.profileEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("user", user)
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment, bundle)
        }
    }
}