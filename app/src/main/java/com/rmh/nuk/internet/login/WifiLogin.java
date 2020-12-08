package com.rmh.nuk.internet.login;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WifiLogin extends AsyncTask<Object, Integer, String> {
    private final WeakReference<String> weakPost;
    private final String Url;

    @SuppressWarnings("deprecation")
    public WifiLogin(String u, String post) {
        weakPost = new WeakReference<>(post);
        Url = u;
    }

    @Override
    protected String doInBackground(Object[] objects) {
        try {
            if (weakPost != null && Url != null) {
                URL url = new URL(Url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(3);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(URLEncoder.encode(weakPost.get(), "UTF-8"));
                out.flush();
                out.close();
                Log.d("RMH", "Success Post");

                InputStream inputStream = conn.getInputStream();
                int status = conn.getResponseCode();
                Log.d("RMH", String.valueOf(status));
                StringBuilder result = new StringBuilder();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    BufferedReader in = new BufferedReader(reader);

                    String line;
                    while ((line = in.readLine()) != null) {
                        result.append(line).append("\n");
                    }
                } else {
                    result = new StringBuilder("Did not work!");
                }
                Log.d("RMH", result.toString());
                assert inputStream != null;
                inputStream.close();
                conn.disconnect();
                return result.toString();
            }
        } catch (Exception e) {
            Log.e("RMH", "Exception");
            e.printStackTrace();
        }
        return "";
    }
}
