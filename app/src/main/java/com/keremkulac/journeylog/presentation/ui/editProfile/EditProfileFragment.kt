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
import com.keremkulac.journeylog.util.HandleResult
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
                binding.userNameInput.setText(it.name)
                binding.userSurnameInput.setText(it.surname)
                binding.userEmailInput.setText(it.email)
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
            HandleResult.handleResult(binding.progressBar, result, onSuccess = { url ->
                Glide.with(requireContext()).load(url).into(binding.profilePicture)
            })
        }
    }

    private fun observeSaveProfilePictureResult() {
        viewModel.saveProfilePictureResult.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result, onSuccess = { data ->
                findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
            })
        }
    }

    private fun observeUpdateUserResult() {
        viewModel.updateUserResult.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                }, onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun getUser(user: User?): User {

        return User(
            id = user!!.id,
            name = binding.userNameInput.text.toString().trim(),
            surname = binding.userSurnameInput.text.toString().trim(),
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
                    observeSaveProfilePictureResult()
                    observeUpdateUserResult()
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
            binding.userNameInput.text.toString().trim(),
            binding.userSurnameInput.text.toString().trim()
        )
        return isValid
    }
}