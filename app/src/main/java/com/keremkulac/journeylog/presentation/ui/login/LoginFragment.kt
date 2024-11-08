package com.keremkulac.journeylog.presentation.ui.login


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentLoginBinding
import com.keremkulac.journeylog.util.AuthResult
import com.keremkulac.journeylog.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login()
        navigateSignup()
        observeLoginResult()
    }

    private fun login(){
        binding.login.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            viewModel.login(email,password)
        }
    }

    private fun observeLoginResult(){
        viewModel.loginResult.observe(viewLifecycleOwner){loginResult->
            when(loginResult){
                is AuthResult.Error -> Toast.makeText(requireContext(),loginResult.error.toString(),Toast.LENGTH_SHORT).show()
                is AuthResult.Success -> findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }
    private fun navigateSignup(){
        binding.register.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }
}