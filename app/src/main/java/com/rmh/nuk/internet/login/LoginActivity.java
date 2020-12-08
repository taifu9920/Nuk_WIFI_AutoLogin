package com.rmh.nuk.internet.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.rmh.nuk.internet.R;

public class LoginActivity extends AppCompatActivity {
    BroadcastReceiver receiveclose = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiveclose);
        Log.d("RMH", "Activity Destroyed");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (KeepAliveService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Post Setting Saved", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = getSharedPreferences("LoginData", MODE_PRIVATE).edit();
                editor.putString("account", usernameEditText.getText().toString());
                editor.putString("password", passwordEditText.getText().toString());
                editor.apply();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED  ){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 123);
            }else{
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED  ){
                    requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, 123); }
            }
        }


        NotificationManager nm = getSystemService(NotificationManager.class);
        NotificationChannel KeepAliveChannel = new NotificationChannel(
                "KeepAlive",
                "Keep Alive",
                NotificationManager.IMPORTANCE_LOW);
        KeepAliveChannel.setDescription("KeepAlive");
        nm.createNotificationChannel(KeepAliveChannel);

        Intent intent = new Intent(getApplication(), KeepAliveService.class);
        if (!isMyServiceRunning()) startService(intent);
        registerReceiver(receiveclose, new IntentFilter("com.rmh.nuk.internet.login.action.close"));
    }
}