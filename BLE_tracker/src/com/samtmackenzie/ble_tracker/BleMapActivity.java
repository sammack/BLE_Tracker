/* 
* Author: Sam MacKenzie
* Repository: https://github.com/sammack/BLE_Tracker
* Email: samtmackenzie@gmail.com
* website: www.samtmackenzie.com  
*  
* The MIT License (MIT)
* 
* Copyright (c) [2014] [Sam MacKenzie]
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

package com.samtmackenzie.ble_tracker;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/* The map sceen displays all of the devices on your list
 * it also displays thier MAC addresses.
 */
public class BleMapActivity extends Activity{
    Context mContext = this;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        // Get a handle to the Map Fragment
        GoogleMap mMap = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        
        // get the current location
        SingleLocation mSingleLocation = new SingleLocation(mContext);
        double[] locationArray = {0,0};
        locationArray = mSingleLocation.getLatestLocation();
        LatLng startPoint = new LatLng(locationArray[0], locationArray[1]);
        
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 13));
        
        // get the arraylist of ble device records passed from the main activity.
        ArrayList<BleDeviceRecord> arrayOfDevice = new ArrayList<BleDeviceRecord>();
        arrayOfDevice = getIntent().getParcelableArrayListExtra("devices");
        
        // go through the list of bleDevices and place them on the map.
        for(BleDeviceRecord index : arrayOfDevice){ 
            LatLng device = new LatLng(index.getBleLat(), index.getBleLong());
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(device, 13));
            mMap.addMarker(new MarkerOptions()
                           .title(index.getName())
                           .snippet(index.getAddress())
                           .position(device));
        } 
    }
}
