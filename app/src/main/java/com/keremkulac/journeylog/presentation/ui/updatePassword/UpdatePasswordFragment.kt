package com.keremkulac.journeylog.presentation.ui.updatePassword

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentUpdatePasswordBinding
import com.keremkulac.journeylog.util.CustomDialog
import com.keremkulac.journeylog.util.HandleResult
import com.keremkulac.journeylog.util.SuccessfulDialogUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdatePasswordFragment : BottomSheetDialogFragment(R.layout.fragment_update_password) {
    private lateinit var binding: FragmentUpdatePasswordBinding
    private val viewModel by viewModels<UpdatePasswordViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpdatePasswordBinding.bind(view)
        changePassword()
        observePasswordResult()
        observeValidationMessage()
    }

    private fun changePassword() {
        binding.confirm.setOnClickListener {
            val oldPassword = binding.userOldPasswordInput.text.toString().trim()
            val newPassword = binding.userNewPasswordInput.text.toString().trim()
            val newPasswordConfirm = binding.userNewPasswordConfirmInput.text.toString().trim()
            if (viewModel.validatePassword(oldPassword, newPassword, newPasswordConfirm)) {
                showDialog(oldPassword, newPassword)
            }
        }
    }

    private fun observePasswordResult() {
        viewModel.updatePasswordResult.observe(viewLifecycleOwner) { updateResult ->
            HandleResult.handleResult(binding.progressBar, updateResult,
                onSuccess = {
                    dismiss()
                    SuccessfulDialogUtil(
                        requireContext(),
                        getString(R.string.dialog_success_password_update_message)
                    ).showDialog()
                }, onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun observeValidationMessage() {
        viewModel.validationMessage.observe(viewLifecycleOwner) { messageResult ->
            Toast.makeText(requireContext(), messageResult, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialog(oldPassword: String, newPassword: String) {
        requireContext().apply {
            CustomDialog.showConfirmationDialog(
                requireContext(),
                getString(R.string.dialog_change_password_title),
                getString(R.string.dialog_change_password_message),
                getString(R.string.dialog_change_password_positive_button_text),
                getString(R.string.dialog_change_password_negative_button_text)
            ) {
                viewModel.updatePassword(oldPassword, newPassword)
            }
        }
    }

}