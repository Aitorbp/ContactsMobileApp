package com.example.contactosapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactosapp.data.server.ApiResponseStatus
import com.example.contactosapp.data.server.Contact
import com.example.contactosapp.data.server.ContactRepository

import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val contactRepository = ContactRepository()

    //Principio de encasulamiento. Hacemos un livedata publico para que solo sea editable desde dentro del vm y nada mas.
    //Desde fuera del vm vamos a poder leer la lista, pero no editarla
    private val _contactsList = MutableLiveData<List<Contact>>()
    val contactsList: LiveData<List<Contact>>
    get() = _contactsList

    private val _status = MutableLiveData<ApiResponseStatus<List<Contact>>>()
    val status: LiveData<ApiResponseStatus<List<Contact>>>
        get() = _status

    init {
        downloadContact()
    }

    private fun downloadContact() {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loaling()
            handleResponseStatus(contactRepository.downloadContacts())
        }
    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<List<Contact>>) {
        if (apiResponseStatus is ApiResponseStatus.Success){
            _contactsList.value = apiResponseStatus.data
        }
        _status.value = apiResponseStatus


    }
}