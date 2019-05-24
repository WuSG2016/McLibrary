package com.wsg.mclib;

import android.os.SystemClock;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import android_serialport_api.SerialPort;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new SerialThread().start();

       new UsbSerialThread().start();
    }



}
