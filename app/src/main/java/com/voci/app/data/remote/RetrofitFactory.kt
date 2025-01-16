package com.voci.app.data.remote

import com.voci.app.data.api.GeocodingInterface
import com.voci.app.data.api.SearchInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    // Base URL for the Mapbox API
    private const val BASE_URL = "https://api.mapbox.com/"
    // Logging interceptor for debugging purposes
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    // OkHttp client with the logging interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    // Retrofit instance for the Search API with the base URL, client, and Gson converter
    val searchApi: SearchInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchInterface::class.java)
    }
    // Retrofit instance for the Geocoding API with the base URL, client, and Gson converter
    val geocodingApi: GeocodingInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingInterface::class.java)
    }
}