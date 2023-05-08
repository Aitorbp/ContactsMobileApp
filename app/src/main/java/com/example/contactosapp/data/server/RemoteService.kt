package com.example.contactosapp.data.server


import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {

    @GET("?results=20")
    suspend fun getContacts(): ContactsResponseModel

}