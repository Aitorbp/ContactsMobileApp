package com.example.contactosapp.data.server

import com.example.contactosapp.data.server.RemoteConnection.service

class ContactRepository {

    suspend fun downloadContacts(): ApiResponseStatus<List<Contact>>
        = makeNetworkCall {
            val contactsResponseModel = service.getContacts()
        contactsResponseModel.results
        }
}