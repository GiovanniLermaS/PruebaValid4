package com.example.pruebavalid.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pruebavalid.data.network.MyApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtistRepository {

    fun artist(url: String): LiveData<String> {
        val artistResponse = MutableLiveData<String>()
        MyApi()
            .getArtist(url)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    artistResponse.value = t.message
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful)
                        artistResponse.value = response.body()?.string()
                    else artistResponse.value = response.errorBody()?.string()

                }
            })
        return artistResponse
    }
}