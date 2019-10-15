package com.example.pruebavalid.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopArtists(
    var artist: ArrayList<Artist>
) {
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}