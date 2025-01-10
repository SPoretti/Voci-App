// RetrofitSetup.kt
package com.example.vociapp.data.remote

import android.location.Address
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

data class NominatimResponse(
    val lat: String,
    val lon: String
)

data class ReverseNominatimResponse(
    val address: Address,
    val display_name: String
)

interface NominatimApiService {
    @GET("search")
    suspend fun geocodeAddress(
        @Query("q") address: String,
        @Query("format") format: String = "json"
    ): List<NominatimResponse>

    @GET("reverse") // Nuovo endpoint per il reverse geocoding
    suspend fun reverseGeocode(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("format") format: String = "json",
        @Query("zoom") zoom: Int = 18 // Livello di dettaglio (più alto è, più è preciso)
    ): ReverseNominatimResponse
}

@Singleton
class GeocodingClient @Inject constructor() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nominatim.openstreetmap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val nominatimService: NominatimApiService = retrofit.create(NominatimApiService::class.java)
}