package com.example.vociapp.data.util

import com.google.firebase.firestore.GeoPoint
import com.mapbox.geojson.Point

/**
 * Represents a waypoint with an ID, name, and location.
 *
 * This data class is used to store information about a specific point of interest
 * or location on a map.
 *
 * @property id The ID of the waypoint, which may be null if it hasn't been assigned yet.
 * @property name The name of the waypoint, which may be null.
 * @property location The geographic location of the waypoint, represented as a GeoPoint.
 */
data class Waypoint(
    val id: String? = null, // Firestore document ID (optional)
    val name: String? = null,
    val location: GeoPoint? = null // Using GeoPoint for Firestore
)

/**
 * A utility class for converting between GeoPoint and Point objects.
 *
 * This class provides methods for converting between GeoPoint objects (used by
 * Firestore) and Point objects (used by Mapbox).
 */
class GeoPointConverter {

    /**
     * Converts a Point object to a GeoPoint object.
     *
     * @param point The Point object to convert.
     * @return The converted GeoPoint object.
     */
    fun fromPointToGeoPoint(point: Point): GeoPoint {
        return GeoPoint(point.latitude(), point.longitude())
    }

    /**
     * Converts a GeoPoint object to a Point object.
     *
     * @param geoPoint The GeoPoint object to convert.
     * @return The converted Point object.
     */
    fun fromGeoPointToPoint(geoPoint: GeoPoint): Point {
        return Point.fromLngLat(geoPoint.longitude, geoPoint.latitude)
    }
}