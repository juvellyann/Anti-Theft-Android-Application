package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link GalleryFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class GalleryFragment extends Fragment {
    ArrayList<Image> fileName = new ArrayList<>();
    ArrayList<Image> sfileName = new ArrayList<>();
    ImageView imageView;


    GalleryAdapter gadapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GalleryFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment GalleryFragment.
//     */
    // TODO: Rename and change types and number of parameters
//    public static GalleryFragment newInstance(String param1, String param2) {
//        GalleryFragment fragment = new GalleryFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        ProgressBar loadingIndicator = rootView.findViewById(R.id.loadingIndicator);
        new HttpRequestTask(loadingIndicator).execute();
        Spinner monthSpinner = rootView.findViewById(R.id.monthSpinner);

        // Create an ArrayAdapter with month items
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, getMonthItems());

        // Set the dropdown layout style
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        monthSpinner.setAdapter(adapter);
        gadapter = new GalleryAdapter(getActivity(), sfileName);
        ListView listView = rootView.findViewById(R.id.listView);
        listView.setAdapter(gadapter);

        // Set a listener for item selection
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = parent.getItemAtPosition(position).toString();
                // Do something with the selected month
                sfileName.clear();
                if(position == 0){
                    sfileName.addAll(fileName);
                }else{
                    int i = 0;
                    for(;i < fileName.size();i++){
                        String[] tokens = fileName.get(i).dateTime.split("-");
                        try{
                            int number = Integer.parseInt(tokens[1]);
                            if(number == position){
                                sfileName.add(fileName.get(i));
                            }
                        }
                        catch (NumberFormatException ex){
                            ex.printStackTrace();
                        }
                    }
                }


                gadapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });
        return rootView;
    }

    private List<String> getMonthItems() {
        List<String> months = new ArrayList<>();
        months.add("Month");
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        return months;
    }

    private void displayImages() {

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
                URL url = new URL("http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=imgs&did=1");

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
            if (responseData != null) {
                Log.d("Gallery", responseData);
                // Process the response data as needed
                JSONArray jsonArray = null;
                sfileName.clear();
                try {
                    jsonArray = new JSONArray(responseData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String deviceId = jsonObject.getString("device_id");
                        String filename = jsonObject.getString("sFilename");
                        String dateTime = jsonObject.getString("dDateTime");

                        // Do something with the parsed data
                        // For example, you can create objects or store the values in a list
                        // or use them as needed in your application
                        Log.d("Image Id",id+"");
                        fileName.add(0,new Image(id,filename,dateTime));
                    }
                    sfileName.addAll(fileName);
                    gadapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            } else {
                // Handle the case when the request fails
            }
        }
    }
}