package com.keremkulac.journeylog.util

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.keremkulac.journeylog.R


class GalleryPermissionManager(
    private val fragment: Fragment,
    private val onImageSelected: (Uri) -> Unit
) {


    private val galleryPermissionLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openGallery()
        } else {
            fragment.requireContext().apply {
                Toast.makeText(this, getString(R.string.warning_gallery_permission_required_message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val galleryActivityResultLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                onImageSelected(uri)
            }
        }
    }

    fun checkGalleryPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                fragment.requireActivity(),
                permission
            ) -> {
                if (!fragment.shouldShowRequestPermissionRationale(permission)) {
                    galleryPermissionLauncher.launch(permission)
                } else {
                    openAppSettingsDialog()
                }
            }

            else -> {
                galleryPermissionLauncher.launch(permission)
            }
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        galleryActivityResultLauncher.launch(galleryIntent)
    }

    private fun openAppSettingsDialog() {
        fragment.requireContext().apply {
            CustomDialog.showConfirmationDialog(
                this,
                getString(R.string.dialog_gallery_permission_title),
                getString(R.string.dialog_gallery_permission_message),
                getString(R.string.dialog_gallery_permission_positive_button_text),
                getString(R.string.dialog_gallery_permission_negative_button_text)
            ) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", fragment.requireContext().packageName, null)
                }
                fragment.startActivity(intent)
            }
        }

    }


}
