// Defines how the app connects to the Picsum API using Retrofit and Moshi.
package com.example.customtoastdemo

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumService {

    // API endpoint: fetches a list of images with pagination
    @GET("v2/list")
    suspend fun getList(
        @Query("page") page: Int, // Page number
        @Query("limit") limit: Int // Number of results per page
    ): List<PicsumPhoto>

    companion object {
        // Creates and configures a Retrofit service for the API
        fun create(): PicsumService {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC // Logs requests for debugging
            }

            // HTTP client that includes the logger
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            // Moshi converter to parse JSON into Kotlin objects
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            // Build Retrofit instance with base URL and Moshi converter
            val retrofit = Retrofit.Builder()
                .baseUrl("https://picsum.photos/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            // Return the service ready for use
            return retrofit.create(PicsumService::class.java)
        }
    }
}
