package com.keremkulac.journeylog.presentation.ui.login


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentLoginBinding
import com.keremkulac.journeylog.util.Result
import com.keremkulac.journeylog.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeIsLoggedUserInResult()
        login()
        navigateSignup()
        observeLoginResult()
        observeValidation()
        sign()
    }

    private fun login() {
        binding.login.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            if (isValid()) {
                viewModel.login(email, password)
            }
        }
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(viewLifecycleOwner) { loginResult ->
            when (loginResult) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), loginResult.error, Toast.LENGTH_SHORT).show()
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), loginResult.data, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }
    }


    private fun observeIsLoggedUserInResult() {
        viewModel.keepUserLoggedIn.observe(viewLifecycleOwner) { isLoggedInResult ->
            when (isLoggedInResult) {
                is Result.Success -> findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                else -> {}
            }
        }
    }

    private fun observeValidation() {
        viewModel.validationMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValid(): Boolean {
        val isValid = viewModel.validateInputs(
            binding.editTextEmail.text.toString().trim(),
            binding.editTextPassword.text.toString().trim()
        )
        return isValid
    }


    private fun navigateSignup() {
        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    private fun sign() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val signClient = GoogleSignIn.getClient(requireContext(), gso)
        binding.loginWithGoogle.setOnClickListener {
            startActivityForResult(signClient.signInIntent, 9001)
            viewModel.loginWithGoogle.observe(viewLifecycleOwner) { result ->
                when (result) {
                    Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Result.Failure -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), result.data, Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }
            }

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                viewModel.loginWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

}