package com.example.pruebavalid.ui.auth

import androidx.lifecycle.LiveData

interface AuthListener {
    fun onStarted()
    fun onSuccess(artistResponse: LiveData<String>)
    fun onFailure(message: String)
}