package com.balckhao.blackhaoutil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.balckhao.blackhaoutil.usbUtil.UsbTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.usb_test:
                Intent usbIntent = new Intent(this, UsbTestActivity.class);
                startActivity(usbIntent);
                break;
        }
    }
}
