package com.keremkulac.journeylog.presentation.ui.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentLoginBinding
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.HandleResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private var backPressedOnce = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed()
        activityResult()
        observeIsLoggedUserInResult()
        login()
        navigateSignup()
        observeLoginResult()
        observeValidation()
        loginWithGoogle()
        observeLoginWithGoogleResult()
        observeRegisterResult()
        forgotPassword()
    }

    private fun login() {
        binding.login.setOnClickListener {
            val email = binding.userEmailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()
            if (isValid()) {
                viewModel.login(email, password)
            }
        }
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(viewLifecycleOwner) { loginResult ->
            HandleResult.handleResult(binding.progressBar, loginResult,
                onSuccess = { data ->
                    Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }, onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun observeIsLoggedUserInResult() {
        viewModel.currentUser.observe(viewLifecycleOwner) { isLoggedInResult ->
            HandleResult.handleResult(binding.progressBar, isLoggedInResult,
                onSuccess = {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                })
        }
    }

    private fun loginWithGoogle() {
        val signClient by lazy {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            GoogleSignIn.getClient(requireContext(), gso)
        }
        binding.loginWithGoogle.setOnClickListener {
            googleSignInLauncher.launch(signClient.signInIntent)
        }
    }

    private fun observeLoginWithGoogleResult() {
        viewModel.loginWithGoogleResult.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    viewModel.register(data as User)
                }, onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun observeRegisterResult() {
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }, onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun forgotPassword() {
        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    private fun activityResult() {
        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    viewModel.loginWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Toast.makeText(requireContext(), e.message ?: "", Toast.LENGTH_SHORT).show()
                }
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
            binding.userEmailInput.text.toString().trim(),
            binding.passwordInput.text.toString().trim()
        )
        return isValid
    }

    private fun navigateSignup() {
        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (backPressedOnce) {
                        requireActivity().finish()
                    } else {
                        backPressedOnce = true
                        requireContext().apply {
                            Toast.makeText(
                                this,
                                getString(R.string.warning_logout_message),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Handler(Looper.getMainLooper()).postDelayed(
                            { backPressedOnce = false },
                            2000
                        )
                    }
                }
            }
        )
    }

}