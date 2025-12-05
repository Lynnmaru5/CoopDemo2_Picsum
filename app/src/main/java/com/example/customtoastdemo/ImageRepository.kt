package com.example.customtoastdemo

// Handles API + local database for images
class ImageRepository(
    private val api: PicsumService,   // Retrofit service for Picsum
    private val dao: ImageDao         // Room DAO for local storage
) {

    // Fetches a list of photos from Picsum API
    suspend fun fetchPhotos(page: Int, limit: Int) = api.getList(page, limit)

    // Saves the selected photo into Room
    suspend fun saveImageLocally(photo: PicsumPhoto) {
        val entity = ImageEntity(
            author = photo.author,
            url = photo.downloadUrl
        )
        dao.insertImage(entity)
    }

    // Loads the last saved image from Room (if any)
    suspend fun loadLastSavedImage(): ImageEntity? {
        return dao.getLastSavedImage()
    }
}
