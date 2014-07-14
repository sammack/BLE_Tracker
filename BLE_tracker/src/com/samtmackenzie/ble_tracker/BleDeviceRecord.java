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

/* Class for each Bluetooth device found.
 * stores the name, address, UUID, location and 
 * the higest RSSI found. 
 * has functions to return all the parameters as well
 * as the UUDI as a string, and a string of all of them.
 */
public class BleDeviceRecord {
    String bleName;
    String bleAddress;
    double[] bleLocation;
    String locationQuality;
    int peakRSSI = -127;
    
    public BleDeviceRecord(BluetoothDevice device, double[] location, 
                           String quality, int newRSSI) {
        bleName = device.getName();
        bleAddress = device.getAddress();
        bleLocation = location;
        peakRSSI = newRSSI;
        locationQuality = quality;
    }
    
    public String getName(){ return bleName; }
    public String getAddress(){ return bleAddress; }
    public int getPeakRSSI(){ return peakRSSI; }
    public double[] getBleLocation(){ return bleLocation; };
    public void setPeakRSSI(int newPeakRSSI){ peakRSSI = newPeakRSSI; }
    
    public void setBleLocation(double[] newLocation, String newQuality){
        bleLocation = newLocation; 
        locationQuality = newQuality;
    }
    
    public String toString(){
        String locaionString = Double.toString(bleLocation[0]) + " " + 
                               Double.toString(bleLocation[1]); 
        return "Name: " + bleName + "\nMAC ADDR: " + bleAddress + "\nPeak RSSI: "  
    	       + peakRSSI + "\nLocation: " + locaionString + "  " + locationQuality + "\n\n";
    } 
}
