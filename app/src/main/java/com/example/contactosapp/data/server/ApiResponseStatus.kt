package com.example.contactosapp.data.server

sealed class ApiResponseStatus<T> {
    class Loaling<T>() : ApiResponseStatus<T>()
    class Error<T>(val menssageId: Int): ApiResponseStatus<T>()
    class Success<T>(val data: T): ApiResponseStatus<T>()
}