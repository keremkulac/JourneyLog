package com.keremkulac.journeylog.util

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraPermissionManager(
    private val fragment: Fragment,
    private val onImageSelected: (Uri) -> Unit
) {
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    private val cameraPermissionRequestLauncher: ActivityResultLauncher<String> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startDefaultCamera()
            } else {
                Toast.makeText(
                    fragment.requireContext(),
                    "Kamera izni gereklidir",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private val takePictureLauncher: ActivityResultLauncher<Intent> =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                onImageSelected(photoUri)
            }
        }

    fun handleCameraPermission() {
        val permission = Manifest.permission.CAMERA
        when {
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                startDefaultCamera()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                fragment.requireActivity(),
                permission
            ) -> {
                if (!fragment.shouldShowRequestPermissionRationale(permission)) {
                    cameraPermissionRequestLauncher.launch(permission)
                } else {
                    openAppSettingsDialog()
                }
            }

            else -> {
                cameraPermissionRequestLauncher.launch(permission)
            }
        }
    }

    private fun startDefaultCamera() {
        photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(
            fragment.requireContext(),
            "${fragment.requireContext().packageName}.fileprovider",
            photoFile
        )

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        takePictureLauncher.launch(cameraIntent)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir =
            fragment.requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    private fun openAppSettingsDialog() {
        CustomDialog.showConfirmationDialog(
            fragment.requireContext(),
            "İzin gerekli",
            "Kameraya erişim izni uygulama ayarlarından manuel olarak etkinleştirilmelidir.",
            "Ayarlar",
            "İptal"
        ) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", fragment.requireContext().packageName, null)
            }
            fragment.startActivity(intent)
        }
    }


}