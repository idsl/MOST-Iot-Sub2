package com.example.popyue.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.a2017617iot1.Login;
import com.a2017617iot1.Login_captcha;
import com.a2017617iot1.Menu_admin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //read the wifiMAC
//        if (getMacAddress(this).equals("2c:4d:54:da:14:e0"))
//        {
//            Log.i("result","abc");
//            Intent intent = new Intent(MainActivity.this,com.a2017617iot1.Login.class);
//
//           // intent.setClass(MainActivity.this,com.a2017617iot1.Login.class);
//            //把字串傳到第二個Activity
//            intent.putExtra("MAC", getMacAddress(this));
//            startActivity(intent);
//            finish();
//            //tvWifiname.setText(getWifiName(this));
//        }
//使用mac位置進行選擇
//        if(getMacAddress(this).equals("2c:4d:54:da:14:e4")){
            Log.i("result","123");
//            Intent intent = new Intent();
            Intent intent=new Intent(MainActivity.this, Menu_admin.class);

//            intent.setClass(MainActivity.this,com.cwsu.client.LoginActivity.class);
            intent.putExtra("MAC", getMacAddress(this));
            startActivity(intent);
            finish();
            // tvWifiname.setText(getWifiName(this));
//        }
    }
    //Get WIFINAME
    public static String getWifiName(Context context){
        WifiManager wifi =(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifi.isWifiEnabled()==false){
            return "123";
        }
        WifiInfo wifiInf = wifi.getConnectionInfo();
        return wifiInf.getSSID();
    }
    //Get MacAddress
    public static String getMacAddress(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifi.isWifiEnabled()==false){
            return "123";
        }
        WifiInfo wifiInf = wifi.getConnectionInfo();
        //return wifiInf.getMacAddress();
        return wifiInf.getBSSID();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
