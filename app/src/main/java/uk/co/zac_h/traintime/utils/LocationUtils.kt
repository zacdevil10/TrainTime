package uk.co.zac_h.traintime.utils

import android.content.Context
import android.location.Geocoder
import java.io.IOException
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * Common methods used for getting location information and calculating distances
 */
object LocationUtils {

    /**
     * Returns the location of the device
     * @param context activity context
     * @return the phones current location
     */
    fun getLocation(context: Context?): LatLng {
        val gpsTracking = GPSTracking(context)

        val location = LatLng(gpsTracking.latitude ?: 51.511050, gpsTracking.longitude ?: -0.104374)

        gpsTracking.unregister()

        return location
    }

    /**
     * Use the location service to check if the user is in London.
     * @param context activity context
     * @param location the user's coordinates
     * @return the phones location, or a dummy location if they are outside London
     */
    fun isInLondon(context: Context?, location: LatLng): LatLng {
        val geocoder = Geocoder(context?.applicationContext, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(location.lat, location.lng, 1)
            val address = addresses[0]

            if (address.locality != "London") {
                //If phone is not in London, default to address within the London area
                location.lat = 51.511050
                location.lng = -0.104374
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }

        return location
    }

    /**
     * Finds the closest point on a line segment to the phones location
     * @param station is a station
     * @param station2 is another station
     * @param currentLocation is the phones current location
     * @return the coordinates of the closest point
     */
    fun closestPoint(station: LatLng?, station2: LatLng?, currentLocation: LatLng): LatLng {
        //Station to current location
        val (lat, lng) = LatLng(currentLocation.lat - station?.lat!!, currentLocation.lng - station.lng)
        //Station 2 to station
        val (lat1, lng1) = LatLng(station2?.lat!! - station.lat, station2.lng - station.lng)

        //Find the square of station 2 to station
        val squareS2S = (lat1.pow(2.0) + lng1.pow(2.0)).toFloat()

        val SC_S2S = (lat * lat1 + lng * lng1).toFloat()

        var t = SC_S2S / squareS2S

        //If out of line segment bounds, restrict area back to line segment
        if (t < 0.0f) t = 0.0f else if (t > 1.0f) t = 1.0f

        //Calculate and return lat and lng coordinates of the closest point
        return LatLng(station.lat + lat1 * t, station.lng + lng1 * t)
    }

    /**
     * Calculate the distance between two points
     * @param latLng is the first point
     * @param latLng2 is the second point
     * @return the distance between the two locations in miles
     */
    fun distance(latLng: LatLng, latLng2: LatLng?): Double {
        val theta = latLng.lng - latLng2!!.lng
        var dist = sin(Math.toRadians(latLng.lat)) * sin(Math.toRadians(latLng2.lat)) + cos(Math.toRadians(latLng.lat)) * cos(Math.toRadians(latLng2.lat)) * cos(Math.toRadians(theta))
        dist = Math.toDegrees(acos(dist)) * 60.0 * 1.1515

        return dist
    }
}