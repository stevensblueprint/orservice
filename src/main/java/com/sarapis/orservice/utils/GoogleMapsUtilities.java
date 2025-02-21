package com.sarapis.orservice.utils;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Value;

public class GoogleMapsUtilities {

    // no need to construct an object of this type
    private GoogleMapsUtilities() {}

    @Value("${GOOGLE_MAPS_API_KEY}")
    private static String GOOGLE_MAPS_API_KEY;

    // Singleton entry point for making Google Geo API requests
    private static GeoApiContext geoApiContext;

    private static GeoApiContext getGeoApiContext() {
        if (geoApiContext == null) {
            geoApiContext = new GeoApiContext.Builder().apiKey(GOOGLE_MAPS_API_KEY).build();
        }
        return geoApiContext;
    }

    // synchronously convert an address (like "1600 Amphitheatre Parkway, Mountain View, CA") into geographic coordinates (like latitude 37.423021 and longitude -122.083739)
    public static LatLng addressToLatLng(String address) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(getGeoApiContext(), address).await();
            return results[0].geometry.location;

        } catch (Exception e) {
            // todo how should this be error handled?
            // could be ApiException, InterruptedException, or IOException
            throw new RuntimeException(e);
        }
    }
}
