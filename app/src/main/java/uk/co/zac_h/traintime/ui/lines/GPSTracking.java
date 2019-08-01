package uk.co.zac_h.traintime.ui.lines;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Class for getting the phones current GPS location
 */
public class GPSTracking implements LocationListener {

    /*When the app requests the current location, due to the app not needing the location more than once at any given time,
      the location listener is quickly unregistered to conserve battery life and thus the location isn't given a chance to update to the current location.

      The app will initially get the last known location, provide that as the current location, then update the location and unregister the listener.
      When the app is closed and then opened again it will get the new last know location.*/

    private final Context context;

    private Location location;
    private LocationManager locationManager;
    private Double latitude;
    private Double longitude;

    private static final long DISTANCE_TO_CHANGE = 0;
    private static final long TIME_TO_UPDATE = 1;

    public GPSTracking(Context context) {
        this.context = context;
        setLocation();
    }

    private void setLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean gpsEnabled = false;
            boolean networkEnabled = false;
            if (locationManager != null) {
                gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }

            if (networkEnabled) setLocationManager(LocationManager.NETWORK_PROVIDER);
            if (gpsEnabled && location == null) setLocationManager(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void setLocationManager(String provider) {
        locationManager.requestLocationUpdates(provider, TIME_TO_UPDATE, DISTANCE_TO_CHANGE, this);
        location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    public double getLatitude() {
        if (location != null) latitude = location.getLatitude();
        return latitude;
    }

    public double getLongitude() {
        if (location != null) longitude = location.getLongitude();
        return longitude;
    }

    public void unregister() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
