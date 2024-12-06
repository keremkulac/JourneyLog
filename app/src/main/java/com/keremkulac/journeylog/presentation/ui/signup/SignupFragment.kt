package com.keremkulac.journeylog.presentation.ui.signup


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentSignupBinding
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.HandleResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    private val viewModel by viewModels<SignupViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register()
        observeRegisterResult()
        observeCreateUserWithEmailAndPasswordResult()
        navigateLogin()
        observeValidation()
    }

    private fun register() {
        binding.register.setOnClickListener {
            val email = binding.userEmail.text.toString().trim()
            val password = binding.userPassword.text.toString().trim()
            if (isValid()) {
                viewModel.createUserWithEmailAndPassword(email, password)
            }
        }
    }

    private fun observeCreateUserWithEmailAndPasswordResult() {
        viewModel.createUserWithEmailAndPassword.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    val user = getUser().apply { id = data }
                    viewModel.register(user)
                }, onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun observeRegisterResult() {
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
                }, onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun getUser(): User {
        return User(
            id = "",
            name = binding.userName.text.toString().trim(),
            surname = binding.userSurname.text.toString().trim(),
            email = binding.userEmail.text.toString().trim()
        )
    }

    private fun observeValidation() {
        viewModel.validationMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValid(): Boolean {
        val isValid = viewModel.validateInputs(
            binding.userName.text.toString().trim(),
            binding.userSurname.text.toString().trim(),
            binding.userEmail.text.toString().trim(),
            binding.userPassword.text.toString().trim()
        )
        return isValid
    }

    private fun navigateLogin() {
        binding.loginText.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }

}