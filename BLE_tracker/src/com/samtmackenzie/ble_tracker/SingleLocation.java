package com.samtmackenzie.ble_tracker;

import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class SingleLocation {
    LocationManager mLocationManager;
    double LatLong[] = {0,0};
    String locationQuality;
    List<String> providers;
    Location latestLocation = null;
    Context mContext;
    
    public SingleLocation(Context mContext){
        this.mContext = mContext;
        LocationManager mLocationManager = (LocationManager) 
                               mContext.getSystemService(Context.LOCATION_SERVICE);
        providers = mLocationManager.getProviders(true);
        // Get the latest location from GPS
        Criteria criteria = new Criteria();
        String bestProvider = mLocationManager.getBestProvider(criteria, false);
        latestLocation = mLocationManager.getLastKnownLocation(bestProvider);
        if (latestLocation != null)
            locationQuality = bestProvider;
    }

    public double[] getLatestLocation(){
        if (latestLocation != null)
        {
            LatLong[0] = latestLocation.getLatitude();
            LatLong[1] = latestLocation.getLongitude();
        }
        return LatLong;
    }
    
    public String getLatestQuality(){
        if(latestLocation == null)
            return "no location";
        return locationQuality;
    }
}
