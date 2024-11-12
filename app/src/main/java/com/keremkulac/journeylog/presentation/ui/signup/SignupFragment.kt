package com.keremkulac.journeylog.presentation.ui.signup


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentSignupBinding
import com.keremkulac.journeylog.util.Result
import com.keremkulac.journeylog.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    private val viewModel by viewModels<SignupViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register()
        observeRegisterResult()
    }

    private fun register() {
        binding.register.setOnClickListener {
            val email = binding.userEmail.text.toString().trim()
            val password = binding.userPassword.text.toString().trim()
            viewModel.register(email, password)
        }
    }


    private fun observeRegisterResult() {
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.data, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                }
            }

        }
    }
}