package com.noteapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.noteapplication.api.NetworkResult
import com.noteapplication.api.UserApi
import com.noteapplication.models.UserRequest
import com.noteapplication.models.UserResponse
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    private var _userRepositoryLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userRepositoryLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userRepositoryLiveData

    suspend fun signIn(userRequest: UserRequest) {
        _userRepositoryLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signIn(userRequest)
        handleResponse(response)
    }

    suspend fun signUp(userRequest: UserRequest) {
        _userRepositoryLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signUp(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userRepositoryLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            if (response.errorBody().toString() is String) {
                _userRepositoryLiveData.postValue(
                    NetworkResult.Error(
                        response.errorBody().toString()
                    )
                )
            } else {
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                _userRepositoryLiveData.postValue(NetworkResult.Error(jsonObj.getString("message")))
            }
        } else {
            _userRepositoryLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}