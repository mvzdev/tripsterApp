package com.mobileapp.tripster.helpers;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

public class LocationManager {

    private FusedLocationProviderClient client;
    private Geocoder geocoder;
    private LocationCallback locationCallback;

    public LocationManager(Activity activity) {
        client = LocationServices.getFusedLocationProviderClient(activity);
        geocoder = new Geocoder(activity);

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    System.out.println(location.getTime());
                }

            }
        };
    }

    private String getLocationAddresses(Location location) {
            if (location != null) {
                try {
                    return getMostProminentAddress(geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1));

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        return null;
    }

    private String getMostProminentAddress(List<Address> addresses) {
        return addresses.get(0).getAddressLine(0);
    }

    public LiveData<String> getLastKnownLocation() {

        final MutableLiveData<String> lastLocation = new MutableLiveData<>();

        client.getLastLocation().addOnSuccessListener(location -> {
            lastLocation.setValue(getLocationAddresses(location));
        });

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        client.requestLocationUpdates(locationRequest, locationCallback, null);

        return lastLocation;
    }

    public void flushLocations() {
        client.flushLocations();
    }
}
