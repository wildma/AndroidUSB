package com.wildma.androidusb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> usbTempRootDirectory = FileUtils.getUSBTempRootDirectory(this);
        for (int i = 0; i < usbTempRootDirectory.size(); i++) {
            String usbRealRootDirectory = FileUtils.getUSBRealRootDirectory(usbTempRootDirectory.get(i));
            Log.d("MainActivity", "usbRealRootDirectory: " + usbRealRootDirectory);
        }
    }
}
