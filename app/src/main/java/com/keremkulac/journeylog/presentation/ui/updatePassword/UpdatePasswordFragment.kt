package com.keremkulac.journeylog.presentation.ui.updatePassword

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentUpdatePasswordBinding
import com.keremkulac.journeylog.util.Result
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
            if (viewModel.validatePassword(oldPassword,newPassword, newPasswordConfirm)) {
                viewModel.updatePassword(oldPassword, newPassword)
            }
        }
    }


    private fun observePasswordResult() {
        viewModel.updatePasswordResult.observe(viewLifecycleOwner) { updateResult ->
            when (updateResult) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), updateResult.error, Toast.LENGTH_SHORT)
                        .show()
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), updateResult.data, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }

    private fun observeValidationMessage() {
        viewModel.validationMessage.observe(viewLifecycleOwner) { messageResult ->
            Toast.makeText(requireContext(), messageResult, Toast.LENGTH_SHORT).show()
        }
    }

}