package com.example.pruebavalid.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ResponseTopArtists(
    var topartists: TopArtists
) {
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}