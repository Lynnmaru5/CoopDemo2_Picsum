// Retrofit service for Animechan API
package com.example.customtoastdemo

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface QuoteService {

    // Fetch a random anime quote
    @GET("api/random")
    suspend fun getRandomQuote(): QuoteResponse

    companion object {
        // Creates Retrofit instance
        fun create(): QuoteService {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC // log requests
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://animechan.xyz/") // WORKING HTTPS BASE URL
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(QuoteService::class.java)
        }
    }
}
