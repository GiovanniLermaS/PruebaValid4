package com.example.pruebavalid.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class Song(
    var name: String? = null,
    var listeners: String? = null,
    var mbid: String? = null,
    var url: String? = null,
    var artist: Artist,
    var image: ArrayList<Image>? = null
) {
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}