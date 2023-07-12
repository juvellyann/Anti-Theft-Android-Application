package com.example.myapplication;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class HomePage extends AppCompatActivity {
    ActivityMainBinding binding;
    boolean hasShown = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        NotificationHelper.createNotificationChannel(this);
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                new HttpRequestTask().execute();
            }
        }, 0, 500);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.location) {
                replaceFragment(new LocationFragment());
            } else if (itemId == R.id.gallery) {
                replaceFragment(new GalleryFragment());
            } else if (itemId == R.id.notifications) {
                replaceFragment(new NotificationsFragment());
            } else if (itemId == R.id.profile) {
                Bundle extras = getIntent().getExtras();
                String id = null;
                if (extras != null) {
                    id = extras.getString("id");
                }
                replaceFragment(new ProfileFragment(id));
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    class HttpRequestTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {

                // Create a URL object with the endpoint you want to make the request to

                URL url = new URL("http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=getDevice&did=1");

                // Create an HttpURLConnection object
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set the request method (GET, POST, etc.)
                connection.setRequestMethod("GET");

                // Get the response code
                int responseCode = connection.getResponseCode();

                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Disconnect the connection
                connection.disconnect();

                // Return the response data
                return response.toString();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String responseData) {
            super.onPostExecute(responseData);

            if (responseData != null) {
                Log.d("HTTP Response", responseData);
                // Process the response data as needed
                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(responseData);
                    JSONObject deviceJson = responseJson.getJSONObject("device");


                    // TO CHANGE TO iDisturbance
                    String iDisturbance = deviceJson.getString("iDisturbance");
                    Log.d("disturbance",iDisturbance);
                    int flag = Integer.parseInt(iDisturbance);
                    if(flag > 0){
                        if(!hasShown){
                            NotificationHelper.showNotification(HomePage.this,"Disturbance","Disturbance Detected");
                            hasShown = true;
                            Log.d("TimerTask",iDisturbance);
                        }
                    }else{
                        hasShown = false;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            } else {
                // Handle the case when the request fails
            }
        }
    }
}
