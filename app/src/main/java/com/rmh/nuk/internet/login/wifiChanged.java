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
        Log.d("RMH", "DEBUG");
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network Wifi = null;
        for (Network n : cm.getAllNetworks()) {
            if (cm.getNetworkInfo(n) != null && cm.getNetworkInfo(n).getTypeName().equals("WIFI"))
                Wifi = n;
        }
        cm.bindProcessToNetwork(Wifi != null ? Wifi : cm.getActiveNetwork());
        if (Wifi != null && cm.getNetworkInfo(Wifi) != null)
            Log.d("RMH", cm.getNetworkInfo(Wifi).toString());
        if (Wifi != null && cm.getNetworkInfo(Wifi) != null) {
            if (cm.getNetworkInfo(Wifi).getState().equals(NetworkInfo.State.CONNECTED) || cm.getNetworkInfo(Wifi).getDetailedState().equals(NetworkInfo.DetailedState.OBTAINING_IPADDR)) {
                Log.d("RMH", "Received");
                Log.d("RMH", cm.getNetworkInfo(Wifi).toString());
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = wifiInfo.getSSID();
                Log.d("RMH", ssid);
                if (ssid.equals("\"OA_2F\"")) {
                    SharedPreferences datas = context.getSharedPreferences("LoginData", Context.MODE_PRIVATE);
                    new WifiLogin("http://140.127.231.253/cgi-bin/ace_web_auth.cgi", String.format("username=%s&userpwd=%s", datas.getString("account", ""), datas.getString("password", ""))).execute();
                    new WifiLogin("http://192.168.242.254/cgi-bin/ace_web_auth.cgi", String.format("username=%s&password=%s", datas.getString("account", ""), datas.getString("password", ""))).execute();
                }
            }
        }
    }
}
