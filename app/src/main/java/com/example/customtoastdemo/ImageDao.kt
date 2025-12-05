package com.example.customtoastdemo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Data Access Object: defines how we save + load images
@Dao
interface ImageDao {

    // Inserts one image into the table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: ImageEntity)

    // Returns the most recently saved image
    @Query("SELECT * FROM saved_images ORDER BY id DESC LIMIT 1")
    suspend fun getLastSavedImage(): ImageEntity?
}
