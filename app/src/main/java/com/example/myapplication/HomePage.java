package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
    boolean hasShown = false, hasShownBatt = false;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        NotificationHelper.createNotificationChannel(this);
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

//        builder = new AlertDialog.Builder(this);
//        SharedPreferences sharedPref = this.getSharedPreferences("override",Context.MODE_PRIVATE);
//        int iParkingOverride = sharedPref.getInt("iParking", -1);
//        int iEngineOverride = sharedPref.getInt("iEngine", -1);
//        Log.d("Override iParking", iParkingOverride+"");
//        Log.d("Override iEngine", iEngineOverride+"");
//
//        if(iParkingOverride != -1 && iEngineOverride != -1){
//            boolean flag = OverrideSettings.override(iParkingOverride+"",iEngineOverride+"");
////            new OverrideSettings().SetEngine().execute(iEngineOverride+"");
////            new SetParking().execute(iParkingOverride+"");
//            if(isEngineLoading && isParkingLoading) {
//                builder.setView(R.layout.loading_dialog_view);
//                builder.setTitle("Syncing");
//                dialog = builder.create();
//                dialog.show();
//            }

//            if(flag){
//                dialog.hide();
//                SharedPreferences shared = getApplicationContext().getSharedPreferences("override", Context.MODE_PRIVATE);
//                shared.edit().remove("iParking").apply();
//                shared.edit().remove("iEngine").apply();
//            }
//        }
        Timer t = new Timer();
        
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                new HttpRequestTask().execute();
            }
        }, 0, 1000);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                new BatteryHttpTask().execute();
            }
        }, 0, 1000);


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
                            NotificationHelper.showNotification(HomePage.this,"Disturbance","Disturbance Detected",1);
                            hasShown = true;
                            Log.d("TimerTask",iDisturbance);
                            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("disturbanceVal",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("iDisturbance", 1);
                            editor.apply();
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

    class BatteryHttpTask extends AsyncTask<Void, Void, String> {

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
                    String iBattery = deviceJson.getString("iBattery");
                    int flag = Integer.parseInt(iBattery);
                    if(flag < 20){
                        if(!hasShownBatt){
                            NotificationHelper.showNotification(HomePage.this,"Battery Low","Warning: Battery is getting low!",2);
                            hasShownBatt = true;
                            Log.d("TimerTask",iBattery);
                        }
                    }else{
                        hasShownBatt = false;
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
