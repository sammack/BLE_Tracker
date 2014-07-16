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


import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/* Class for each Bluetooth device found.
 * stores the name, address, UUID, location and 
 * the higest RSSI found. 
 * has functions to return all the parameters as well
 * as the UUDI as a string, and a string of all of them.
 */
public class BleDeviceRecord implements Parcelable{
    private String bleName;
    private String bleAddress;
    private double latitude;
    private double longitude;
    private String locationQuality;
    private int peakRSSI = -127;
    
    public BleDeviceRecord(BluetoothDevice device, double[] location, 
                           String quality, int newRSSI) {
        bleName = device.getName();
        bleAddress = device.getAddress();
        latitude = location[0];
        longitude = location[1];
        peakRSSI = newRSSI;
        locationQuality = quality;
    }
    
    public String getName(){ return bleName; }
    public String getAddress(){ return bleAddress; }
    public int getPeakRSSI(){ return peakRSSI; }
    public double getBleLat(){ return latitude; }
    public double getBleLong(){ return longitude; }
    public double[] getBleLocation(){ 
        double[] bleLocation = {latitude, longitude};
        return bleLocation; 
        }
    public void setPeakRSSI(int newPeakRSSI){ peakRSSI = newPeakRSSI; }
    
    public void setBleLocation(double[] newLocation, String newQuality){
        latitude = newLocation[0];
        longitude = newLocation[0];
        locationQuality = newQuality;
    }
    
    public String toString(){
        String locaionString = Double.toString(latitude) + " " + 
                               Double.toString(longitude); 
        return "Name: " + bleName + "\nMAC ADDR: " + bleAddress + "\nPeak RSSI: "  
    	       + peakRSSI + "\nLocation: " + locaionString + "  " + locationQuality + "\n\n";
    }
    
    public BleDeviceRecord(Parcel in) {
        bleName = in.readString();
        bleAddress = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        locationQuality = in.readString();
        peakRSSI = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(bleName);
        out.writeString(bleAddress);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeString(locationQuality);
        out.writeInt(peakRSSI);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<BleDeviceRecord> CREATOR = new Parcelable.Creator<BleDeviceRecord>() {
        public BleDeviceRecord createFromParcel(Parcel in) {
            return new BleDeviceRecord(in);
        }
        public BleDeviceRecord[] newArray(int size) {
            return new BleDeviceRecord[size];
        }
    };
}
