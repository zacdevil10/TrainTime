package uk.co.zac_h.traintime.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

/**
 * Class for getting the phones current GPS location
 */
class GPSTracking(
    /*When the app requests the current location, due to the app not needing the location more than once at any given time,
      the location listener is quickly unregistered to conserve battery life and thus the location isn't given a chance to update to the current location.

      The app will initially get the last known location, provide that as the current location, then update the location and unregister the listener.
      When the app is closed and then opened again it will get the new last known location.*/

    private val context: Context?
) : LocationListener {

    private var location: Location? = null
    private lateinit var locationManager: LocationManager
    var latitude: Double? = null
    var longitude: Double? = null

    init {
        setLocation()
    }

    private fun setLocation() {
        try {
            locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) setLocationManager(LocationManager.NETWORK_PROVIDER)
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && location == null) setLocationManager(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("MissingPermission")
    private fun setLocationManager(provider: String) {
        locationManager.requestLocationUpdates(provider,
            TIME_TO_UPDATE, DISTANCE_TO_CHANGE.toFloat(), this)
        location = locationManager.getLastKnownLocation(provider)
        if (location != null) {
            latitude = location?.latitude
            longitude = location?.longitude
        }
    }

    fun unregister() {
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location) {
        this.location = location

        latitude = location.latitude
        longitude = location.longitude
    }

    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

    }

    override fun onProviderEnabled(s: String) {

    }

    override fun onProviderDisabled(s: String) {

    }

    companion object {
        private const val DISTANCE_TO_CHANGE: Long = 0
        private const val TIME_TO_UPDATE: Long = 1
    }
}
