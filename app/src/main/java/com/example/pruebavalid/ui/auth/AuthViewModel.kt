package com.example.pruebavalid.ui.auth

import androidx.lifecycle.ViewModel
import com.example.pruebavalid.data.repositories.ArtistRepository

class AuthViewModel : ViewModel() {

    var authListener: AuthListener? = null

    fun getArtist() {
        authListener?.onStarted()
        val artistResponse =
            ArtistRepository().artist("?method=geo.gettopartists&country=spain&api_key=829751643419a7128b7ada50de590067&format=json")
        authListener?.onSuccess(artistResponse)
    }
}