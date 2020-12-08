package com.rmh.nuk.internet.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StopProcess extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RMH", "Triggered");
        Intent it = (Intent) intent.getExtras().get("service");
        if (it != null) context.stopService(it);
        else Log.e("RMH", "it is null!");
    }
}
