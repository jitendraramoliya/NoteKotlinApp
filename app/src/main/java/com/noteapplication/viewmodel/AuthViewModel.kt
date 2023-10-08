package com.noteapplication.viewmodel

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noteapplication.api.NetworkResult
import com.noteapplication.models.UserRequest
import com.noteapplication.models.UserResponse
import com.noteapplication.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userRepositoryLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.signUp(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.signIn(userRequest)
        }
    }

    fun validateUserInput(userRequest: UserRequest, isLogin: Boolean): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (TextUtils.isEmpty(userRequest.email) || TextUtils.isEmpty(userRequest.password)
            || !isLogin && TextUtils.isEmpty(userRequest.username)
        ) {
            result = Pair(false, "Please Enter valid input")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userRequest.email).matches()) {
            result = Pair(false, "Please Enter valid email address")
        } else if (userRequest.password.length < 5) {
            result = Pair(false, "Password should be no less than 5 character")
        }

        return result
    }
}