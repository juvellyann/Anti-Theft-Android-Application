package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.OnMapReadyCallback;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link LocationFragment#newInstance} factory method to
// * create an instance of this fragment.
// */

public class LocationFragment extends Fragment {
    GoogleMap googleMap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//        Fragment fragment = new Fragment();
//        getChildFragmentManager().beginTransaction().commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button getLocationBtn;
        View view = inflater.inflate(R.layout.fragment_location,container,false);
        getLocationBtn = view.findViewById(R.id.button);

        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click here
                // Perform any action or launch other activities/fragments
                Log.d("Click Test","HI");
                new HttpRequestTask().execute();
            }
        });


        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.My_Map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {


            @Override
            public void onMapReady(@NonNull GoogleMap gMap) {

                googleMap = gMap;
                LatLng latLng = new LatLng(37.7749, -122.4194); // Set the initial location coordinates

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude+" KG " + latLng.longitude);
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
                googleMap.addMarker(markerOptions);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(latLng.latitude+" KG " + latLng.longitude);
                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });
//        return inflater.inflate(R.layout.fragment_location, container, false);
        return view;
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
                    String sGeoLocation = deviceJson.getString("sGeoLocation");
                    String[] coordinates = sGeoLocation.split(",");
                    double latitude = Double.parseDouble(coordinates[0]);
                    double longitude = Double.parseDouble(coordinates[1]);

                    LatLng newLatLng = new LatLng(latitude, longitude);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 12));
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(newLatLng).title("New Location"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }



                // Update the UI if necessary
                // getActivity().runOnUiThread(new Runnable() {
                //     @Override
                //     public void run() {
                //         // Update the UI with the response data
                //     }
                // });
            } else {
                // Handle the case when the request fails
            }
        }
    }

}


