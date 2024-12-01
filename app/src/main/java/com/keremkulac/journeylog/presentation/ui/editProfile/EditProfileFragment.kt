package com.keremkulac.journeylog.presentation.ui.editProfile

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentEditProfileBinding
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.util.CameraPermissionManager
import com.keremkulac.journeylog.util.CustomDialog
import com.keremkulac.journeylog.util.GalleryPermissionManager
import com.keremkulac.journeylog.util.Result
import com.keremkulac.journeylog.util.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class EditProfileFragment : BottomSheetDialogFragment(R.layout.fragment_edit_profile) {
    private val viewModel by viewModels<EditProfileViewModel>()
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var galleryPermissionManager: GalleryPermissionManager
    private lateinit var cameraPermissionManager: CameraPermissionManager
    private var uri: Uri? = null
    private var user: User? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditProfileBinding.bind(view)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        observeValidation()
        fillFields()
        getImageUri()
        selectPicture()
        updateUser()
        observeProfilePictureUrl()
    }

    private fun getImageUri() {
        galleryPermissionManager = GalleryPermissionManager(this) { uri ->
            binding.profilePicture.setImageURI(uri)
            this.uri = uri
        }

        cameraPermissionManager = CameraPermissionManager(this) { uri ->
            binding.profilePicture.setImageURI(uri)
            this.uri = uri
        }
    }


    private fun fillFields() {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { user ->
            user?.let {
                this.user = user
                binding.userName.setText(it.name)
                binding.userSurname.setText(it.surname)
                binding.userEmail.setText(it.email)
                if (it.imageUri.isNotEmpty()) {
                    viewModel.getProfilePictureUrl(it.imageUri)
                }
            }
        }

    }

    private fun selectPicture() {
        binding.profilePicture.setOnClickListener {
            requireContext().apply {
                CustomDialog.showConfirmationDialog(
                    this,
                    getString(R.string.dialog_select_picture_title),
                    getString(R.string.dialog_select_picture_message),
                    getString(R.string.dialog_select_picture_positive_button_text),
                    getString(R.string.dialog_select_picture_negative_button_text),
                    onNegativeClick = {
                        galleryPermissionManager.checkGalleryPermission()
                    },
                    onPositiveClick = {
                        cameraPermissionManager.handleCameraPermission()

                    }
                )
            }
        }

    }

    private fun observeProfilePictureUrl() {
        viewModel.getProfilePictureUrlResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Glide.with(requireContext()).load(result.data).into(binding.profilePicture)
                }

                else -> {}
            }
        }
    }

    private fun observeSavaProfilePictureResult() {
        viewModel.saveProfilePictureResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.data, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                }
            }
        }
    }


    private fun observeUpdateUserResult() {
        viewModel.updateUserResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.data, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                }
            }
        }
    }

    private fun getUser(user: User?): User {

        return User(
            id = user!!.id,
            name = binding.userName.text.toString().trim(),
            surname = binding.userSurname.text.toString().trim(),
            email = user.email,
            imageUri = user.imageUri
        )
    }

    private fun updateUser() {
        binding.update.setOnClickListener {
            if (isValid()) {
                val updatedUser = getUser(user)
                if (uri == null) {
                    viewModel.updateUser(updatedUser)
                    sharedViewModel.updateData(updatedUser)
                    observeUpdateUserResult()
                } else {
                    val path = UUID.randomUUID().toString() + ".jpeg"
                    updatedUser.imageUri = path
                    sharedViewModel.updateData(updatedUser)
                    viewModel.saveProfilePicture(uri!!, path)
                    viewModel.updateUser(updatedUser)
                    observeSavaProfilePictureResult()
                }
            }
        }
    }

    private fun observeValidation() {
        viewModel.validationMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValid(): Boolean {
        val isValid = viewModel.validateInputs(
            binding.userName.text.toString().trim(),
            binding.userSurname.text.toString().trim()
        )
        return isValid
    }
}