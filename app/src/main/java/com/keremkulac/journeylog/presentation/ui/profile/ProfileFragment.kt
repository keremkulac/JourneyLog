package com.keremkulac.journeylog.presentation.ui.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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

    private var user: User? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        signOut()
        observeSharedData()
        observeSignOutResult()
        passwordChange()
        selectPicture()
        observeSaveProfilePictureResult()
        observeGetProfilePictureUrlResult()
    }

    private fun observeSharedData() {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { user ->
            this.user = user
            setUserFields()
            viewModel.getProfilePictureUrl(user.imageUri)
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
            checkGalleryPermission()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        galleryActivityResultLauncher.launch(galleryIntent)
    }

    private val galleryPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(requireContext(), "Galeri izni gereklidir", Toast.LENGTH_SHORT).show()
            }
        }

    private val galleryActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    binding.profilePicture.setImageURI(uri)
                    val path = viewModel.createUUID() + ".jpeg"
                    viewModel.saveProfilePicture(uri, path)
                    user?.let {
                        it.imageUri = path
                        viewModel.updateUser(it)
                    }
                }
            }
        }

    private fun checkGalleryPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission) ->{
                if(!shouldShowRequestPermissionRationale(permission)){
                    galleryPermissionLauncher.launch(permission)
                }else{
                    openAppSettingsDialog()
                }
            }

            else -> {
                galleryPermissionLauncher.launch(permission)
            }
        }
    }

    private fun openAppSettingsDialog() {
        CustomDialog.showConfirmationDialog(
            requireContext(),
            "İzin gerekli",
            "Galeriye erişim izni uygulama ayarlarından manuel olarak etkinleştirilmelidir.",
            "Ayarlar",
            "İptal"){
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", requireContext().packageName, null)
            }
            startActivity(intent)
        }
    }


    private fun setUserFields() {
        user?.let {
            if (it.imageUri != "") {
                Glide.with(requireContext()).load(it.imageUri).into(binding.profilePicture)
            }
            binding.userFullName.text = viewModel.formatFullName(it.name, it.surname)
            binding.userEmail.text = it.email
        }
    }

}