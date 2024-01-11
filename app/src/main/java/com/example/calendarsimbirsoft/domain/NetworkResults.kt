package com.example.calendarsimbirsoft.domain

sealed class NetworkResults<T> {
    data class Success<T>(val result: T) : NetworkResults<T>()
    data class Error<T>(val errorText: String) : NetworkResults<T>()
}
