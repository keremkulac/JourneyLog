package com.keremkulac.journeylog.presentation.ui.forgotPassword

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentForgotPasswordBinding
import com.keremkulac.journeylog.util.CustomDialog
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : BottomSheetDialogFragment(R.layout.fragment_forgot_password) {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel by viewModels<ForgotPasswordViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForgotPasswordBinding.bind(view)
        forgotPassword()
        observeForgotPasswordResult()
    }

    private fun forgotPassword() {
        binding.send.setOnClickListener {
            val email = binding.userEmail.text.toString()
            if (email.isNotEmpty()) {
                showDialog(email)
            } else {
                requireContext().apply {
                    Toast.makeText(this, getString(R.string.warning_please_enter_email), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeForgotPasswordResult() {
        viewModel.forgotPasswordResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.data, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }

    private fun showDialog(email: String) {
        requireContext().apply {
            CustomDialog.showConfirmationDialog(
                this,
                getString(R.string.dialog_forgot_password_title),
                getString(R.string.dialog_forgot_password_message),
                getString(R.string.dialog_forgot_password_positive_button_text),
                getString(R.string.dialog_forgot_password_negative_button_text)
            ) {
                viewModel.forgotPassword(email)
            }
        }
    }


}