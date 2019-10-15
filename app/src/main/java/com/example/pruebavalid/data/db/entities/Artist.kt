package com.example.pruebavalid.data.db.entities

import androidx.room.Entity

@Entity
data class Artist(
    var name: String? = null,
    var mbid: String? = null,
    var url: String? = null
) {
}