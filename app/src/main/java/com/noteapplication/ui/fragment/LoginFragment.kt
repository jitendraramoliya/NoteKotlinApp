package com.noteapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.noteapplication.R
import com.noteapplication.api.NetworkResult
import com.noteapplication.databinding.FragmentLoginBinding
import com.noteapplication.models.UserRequest
import com.noteapplication.utils.TokenManager
import com.noteapplication.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            it.findNavController().popBackStack()
            //it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener {
            val validateResult = authViewModel.validateUserInput(getUserRequest(), true)
            if (validateResult.first) {
                val userRequest = getUserRequest()
                authViewModel.loginUser(userRequest)
            } else {
                binding.txtError.text = validateResult.second
            }
        }

        bindObserver()
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.pbProgressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken("12345")
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is NetworkResult.Loading -> {
                    binding.pbProgressBar.isVisible = true
                }
            }
        })
    }

    private fun getUserRequest(): UserRequest {
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        return UserRequest(email, password, "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}