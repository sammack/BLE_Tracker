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
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BleDeviceRecord> bleDevices = new ArrayList<BleDeviceRecord>(); 
    Context mContext = this;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initalize the Bluetooth adapter and get a reference
        final BluetoothManager bluetoothManager =
             (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // check if it is a valid bluetooth adapter
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // check if bluetooth is enabled, if it isn't, ask to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
    }

    @Override
    public void onResume() {
        super.onResume(); 
        // start scanning for Bluetooth devices
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }
    
    @Override
    protected void onStop(){
        super.onStop();
    }
    
    @Override
    public void onDestroy()
    {
            super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { getMenuInflater()
                   .inflate(R.menu.main_activity_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {     
        int id = item.getItemId();
        if (id == R.id.action_exit) {
            finish();
            return true;
        }
        if (id == R.id.action_map) {
            // the map option was selected to pass the arraylist 
            // of ble devices to the mapScreen activity 
            Intent mapScreen = new Intent().setClass(getApplicationContext(), BleMapActivity.class);
            mapScreen.putParcelableArrayListExtra("devices", bleDevices);
            startActivity(mapScreen);
            return true;
        }
        return super.onOptionsItemSelected(item);         
    }
    
 // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                byte[] scanRecord) {
            runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   Boolean newRecord = true;
                   String allDeviceString = "";
                   for(BleDeviceRecord index : bleDevices){
                       // check if there this one already exists by comparing MAC address
                       // if it does exist check if it's RSSI is now higher and update it.
                       // finally check if you now have a valid location where before it was 0,0
                       if( index.getAddress().equals(device.getAddress()) ){
                           newRecord = false;
                           //Check if the new RSSI is higher than in the record
                           if(index.getPeakRSSI() < rssi)
                               index.setPeakRSSI(rssi);
                           // check if the record has a valid location
                           if(index.getBleLocation()[0] == 0 ){
                               SingleLocation mSingleLocation = new SingleLocation(mContext);
                               index.setBleLocation(mSingleLocation.getLatestLocation(), 
                                                    mSingleLocation.getLatestQuality());
                           }
                       }
                       // add the record to the string you will be printing on the screen.
                       allDeviceString += index.toString();
                   }
                   // if it didn't match any of them create a new device and add
                   // it to the list, then print it to the screen too.
                   if(newRecord == true){
                       SingleLocation mSingleLocation = new SingleLocation(mContext);
                       double[] locationArray = {0,0};
                       locationArray = mSingleLocation.getLatestLocation();
                       BleDeviceRecord mBleDeviceRecord = new BleDeviceRecord(device, locationArray, 
                                                          mSingleLocation.getLatestQuality(), rssi);
                       bleDevices.add(mBleDeviceRecord);
                       allDeviceString += mBleDeviceRecord.toString();
                   }
                   // write the records into the text view.
                   TextView textView2 = (TextView) findViewById(R.id.ble_device_parameters);
                   textView2.setText(allDeviceString);
               }
           });
       }
    };
}
