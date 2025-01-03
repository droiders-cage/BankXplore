package com.example.bankx_plore.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "http://34.28.208.64:8080"

    // Create a logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Retrofit instance for requests that don't need authentication (e.g., login)
    val api: ApiService by lazy {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)  // Set connection timeout
            .readTimeout(10, TimeUnit.SECONDS)     // Set read timeout
            .writeTimeout(10, TimeUnit.SECONDS)    // Set write timeout
            .addInterceptor(loggingInterceptor)    // Add the logging interceptor to OkHttpClient
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // Retrofit instance for requests that need authentication (using token)
    fun create(tokenProvider: TokenProvider): ApiService {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)  // Set connection timeout
            .readTimeout(10, TimeUnit.SECONDS)     // Set read timeout
            .writeTimeout(10, TimeUnit.SECONDS)    // Set write timeout
            .addInterceptor(AuthInterceptor(tokenProvider))  // Add the AuthInterceptor
            .addInterceptor(loggingInterceptor)  // Add the logging interceptor to OkHttpClient
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
