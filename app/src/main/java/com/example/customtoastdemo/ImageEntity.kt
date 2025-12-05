package com.example.customtoastdemo

import androidx.room.Entity
import androidx.room.PrimaryKey

// Room table that stores a saved image (author + URL)
@Entity(tableName = "saved_images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // auto ID
    val author: String,                               // saved photographer name
    val url: String                                   // saved image URL
)
