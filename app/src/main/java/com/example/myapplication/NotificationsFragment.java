package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    static ArrayList<Notification> notifications = new ArrayList<>();
    Button disturbanceButton, batteryButton;
    NotificationAdapter nadapter;
    static private HttpRequestTask httpRequestTask;
    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProgressBar loadingIndicator = getView().findViewById(R.id.loadingIndicator);
        httpRequestTask = new NotificationsFragment.HttpRequestTask(loadingIndicator);
        httpRequestTask.execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        ProgressBar loadingIndicator = rootView.findViewById(R.id.loadingIndicator);
        new NotificationsFragment.HttpRequestTask(loadingIndicator).execute();
        ListView listView = rootView.findViewById(R.id.notificationListView);
        nadapter = new NotificationAdapter(getActivity(),notifications,getFragmentManager());
        listView.setAdapter(nadapter);
        return rootView;
    }

    class HttpRequestTask extends AsyncTask<Void, Void, String> {
        private ProgressBar loadingIndicator;

        public HttpRequestTask(ProgressBar loadingIndicator) {
            this.loadingIndicator = loadingIndicator;
        }

        @Override
        protected void onPreExecute() {
            // Show the loading indicator before starting the background task

            loadingIndicator.setVisibility(View.VISIBLE);
        }
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
            loadingIndicator.setVisibility(View.GONE);

            NotificationHelper.createNotificationChannel(getContext());
            if (responseData != null) {
                Log.d("Get Device", responseData);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseData);
                    JSONObject deviceObject = jsonObject.getJSONObject("device");

                    // Get the values of disturbance and isBattery
                    int disturbance = deviceObject.getInt("iDisturbance");
                    int battery = deviceObject.getInt("iBattery");
                    notifications.clear();
                    if(disturbance == 0){
                        notifications.add(new Notification("Disturbance", "Disturbance Detected "));
                        NotificationHelper.showNotification(getContext(),"Disturbance","Disturbance Detected");
                        nadapter.notifyDataSetChanged();
                    }

                    if(battery >= 20){
                        notifications.add(new Notification("Battery", "Battery Low "));
                        nadapter.notifyDataSetChanged();
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