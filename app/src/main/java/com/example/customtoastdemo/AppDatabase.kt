package com.example.customtoastdemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Room database that holds the saved_images table
@Database(entities = [ImageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Exposes the DAO to the rest of the app
    abstract fun imageDao(): ImageDao

    companion object {
        // Singleton instance so we don't create DB multiple times
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Returns the single database instance for the whole app
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "demo2_picsum_db"       // db file name
                ).build().also { db ->
                    INSTANCE = db
                }
            }
        }
    }
}
