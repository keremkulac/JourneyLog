package com.keremkulac.journeylog.presentation.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ApiException
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentProfileBinding
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.CustomDialog
import com.keremkulac.journeylog.util.Result
import com.keremkulac.journeylog.util.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private lateinit var sharedViewModel: SharedViewModel
    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var user: User? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        signOut()
        resultLauncher()
        observeSharedData()
        observeSignOutResult()
        passwordChange()
        selectPicture()
        observeSaveProfilePictureResult()
    }

    private fun observeSharedData() {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { user ->
            this.user = user
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
                    Toast.makeText(requireContext(), result.data, Toast.LENGTH_SHORT).show()
                }
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
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            galleryIntent.type = "image/*"
            resultLauncher.launch(galleryIntent)
        }
    }

    private fun resultLauncher() {
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                Glide.with(this).load(imageUri).into(binding.profilePicture)
                try {
                    val path = viewModel.createUUID() + ".jpeg"
                    viewModel.saveProfilePicture(imageUri!!, path)
                    user?.imageUri = path
                    user?.let { viewModel.updateUser(it) }
                } catch (e: ApiException) {
                    Toast.makeText(requireContext(), e.message ?: "", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}