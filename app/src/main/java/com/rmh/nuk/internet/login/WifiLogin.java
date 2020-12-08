package com.rmh.nuk.internet.login;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WifiLogin extends AsyncTask<Object, Integer, String> {
    private WeakReference<Context> weakContext;
    private WeakReference<String> weakPost;
    private String Url;
    public WifiLogin(Context context, String u, String post) {
        weakContext = new WeakReference<>(context);
        weakPost = new WeakReference<>(post);
        Url = u;
    }
    
    @Override
    protected String doInBackground(Object[] objects) {
        try {
            if (weakPost != null && Url != null){
                URL url = new URL(Url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(URLEncoder.encode(weakPost.get(), "UTF-8"));
                out.flush();
                out.close();
                Log.d("RMH", "Success Post");

                InputStream inputStream = conn.getInputStream();
                int status = conn.getResponseCode();
                Log.d("RMH", String.valueOf(status));
                String result = "";
                if(inputStream != null){
                    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    BufferedReader in = new BufferedReader(reader);

                    String line="";
                    while ((line = in.readLine()) != null) {
                        result += (line+"\n");
                    }
                } else{
                    result = "Did not work!";
                }
                Log.d("RMH", result);
                return result;
            }
        }catch (MalformedURLException e) {
            Log.e("RMH", "MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("RMH", "IOException");
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result.contains("/login_online_detail.php?show_portal=1&orig_referer=&login_page=")) Toast.makeText(weakContext.get(), "Wifi Login Successed!", Toast.LENGTH_SHORT).show();
        else Toast.makeText(weakContext.get(), "Wifi Login Failed...", Toast.LENGTH_SHORT).show();
    }
}
