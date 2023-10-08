package com.noteapplication.api

import com.noteapplication.models.Note
import com.noteapplication.models.NoteRequest
import retrofit2.Response
import retrofit2.http.*

interface NoteApi {

    @GET("/note")
    suspend fun getNotes(): Response<List<Note>>

    @POST("/note")
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<Note>

    @PUT("/note/{noteID}")
    suspend fun updateNote(
        @Path("noteID") noteId: String,
        @Body noteRequest: NoteRequest
    ): Response<Note>

    @DELETE("/note/{noteID}")
    suspend fun deleteNote(@Path("noteID") noteId: String): Response<Note>

}