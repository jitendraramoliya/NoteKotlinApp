package com.noteapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.noteapplication.api.NetworkResult
import com.noteapplication.api.NoteApi
import com.noteapplication.models.Note
import com.noteapplication.models.NoteRequest
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteApi: NoteApi) {

    private var _noteListMutableLiveData = MutableLiveData<NetworkResult<List<Note>>>()
    val noteListMutableLiveData: LiveData<NetworkResult<List<Note>>>
        get() = _noteListMutableLiveData

    private var _noteMutableLiveData = MutableLiveData<NetworkResult<String>>()
    val noteMutableLiveData: LiveData<NetworkResult<String>>
        get() = _noteMutableLiveData

    suspend fun getNotes() {
        _noteListMutableLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.getNotes()
        if (response.isSuccessful && response.body() != null) {
            _noteListMutableLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _noteListMutableLiveData.postValue(NetworkResult.Error("Something went wrong"))
        } else {
            _noteListMutableLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest) {
        val response = noteApi.createNote(noteRequest)
        handleResponse(response, "Note has been created")
    }


    suspend fun updateNote(noteID: String, noteRequest: NoteRequest) {
        val response = noteApi.updateNote(noteID, noteRequest)
        handleResponse(response, "Note has been updated")
    }

    suspend fun deleteNote(noteID: String) {
        val response = noteApi.deleteNote(noteID)
        handleResponse(response, "Note has been deleted")
    }

    private fun handleResponse(response: Response<Note>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _noteMutableLiveData.postValue(NetworkResult.Success(message))
        } else {
            _noteMutableLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

}