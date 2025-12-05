// Represents a single photo item returned by the Picsum API.
package com.example.customtoastdemo

import com.squareup.moshi.Json

data class PicsumPhoto(
    val id: String, // Unique photo ID
    val author: String, // Photographer’s name
    val width: Int, // Image width
    val height: Int, // Image height
    val url: String, // Original photo page URL
    @Json(name = "download_url")
    val downloadUrl: String // Direct link to the image file
)
