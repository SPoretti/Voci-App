package com.example.vociapp.data.util

import com.google.firebase.firestore.GeoPoint
import com.mapbox.geojson.Point

data class Waypoint(
    val id: String? = null, // Firestore document ID (optional)
    val name: String? = null,
    val location: GeoPoint? = null // Using GeoPoint for Firestore
)

class GeoPointConverter {
    fun fromPointToGeoPoint(point: Point): GeoPoint {
        return GeoPoint(point.latitude(), point.longitude())
    }

    fun fromGeoPointToPoint(geoPoint: GeoPoint): Point {
        return Point.fromLngLat(geoPoint.longitude, geoPoint.latitude)
    }
}