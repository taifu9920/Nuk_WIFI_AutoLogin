package com.rmh.nuk.internet.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class wifiChanged extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RMH", "Received");
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network Wifi = null;
        for(Network n : cm.getAllNetworks()){ if (cm.getNetworkInfo(n).getTypeName().equals("WIFI")) Wifi = n; Log.d("RMH", cm.getNetworkInfo(n).getState().toString()); }
        cm.bindProcessToNetwork(Wifi != null ? Wifi : cm.getActiveNetwork());
        if(Wifi != null && cm.getNetworkInfo(Wifi).getState().equals(NetworkInfo.State.CONNECTED)) {
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            Log.d("RMH", ssid);
            if (ssid.equals("\"OA_2F\"")) {
                SharedPreferences datas = context.getSharedPreferences("LoginData", Context.MODE_PRIVATE);
                new WifiLogin(context, "http://140.127.231.253/cgi-bin/ace_web_auth.cgi", String.format("username=%s&userpwd=%s", datas.getString("account", ""), datas.getString("password", ""))).execute();
            }
        }
    }
}
