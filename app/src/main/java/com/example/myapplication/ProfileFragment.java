package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ProfileFragment newInstance} factory method to
// * create an instance of this fragment.
// */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    String id = null, email = null, fullName = null, userName = null, contact= null, brand = null, emergency = null;
    Button edit,logout;
    TextView Email, Username, FullName, ContactNo, Brand, EmergencyContactNum;

    public ProfileFragment(String id) {
        // Required empty public constructor
        this.id = id;
    }

    public String setMyArgument(String Id) {
        this.email= Id;
        return Id;
    }

//    public static ProfileFragment newInstance(String Id){
//        ProfileFragment profileFragment = new ProfileFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("userId", Id);
//        profileFragment.setArguments(bundle);
//        return profileFragment;
//    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ProfileFragment.
//     */
    // TODO: Rename and change types and number of parameters
//    public static ProfileFragment newInstance(String param1, String param2) {
//        ProfileFragment fragment = new ProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1) {
            // Retrieve the data from the Intent
            fullName = data.getStringExtra("fullName");
            userName = data.getStringExtra("username");
            contact = data.getStringExtra("contact");
            brand = data.getStringExtra("brand");
            emergency = data.getStringExtra("emergency");

            Email.setText(email);
            FullName.setText(fullName);
            ContactNo.setText(contact);
            Username.setText(userName);
            Brand.setText(brand);
            EmergencyContactNum.setText(emergency);
        }
    }


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        if(getArguments() != null){
            email = getArguments().getString("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ConnectionHelper connectionHelper = new ConnectionHelper(getContext());
        if(!connectionHelper.haveNetworkConnection()){
            View view = inflater.inflate(R.layout.no_internet_message, container, false);
            TextView ttl = view.findViewById(R.id.noNetTtl);
            ttl.setVisibility(View.VISIBLE);
            ttl.setText("Profile");
            return view;
        }
        new HttpRequestTask().execute();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        edit = view.findViewById(R.id.changePasswordBtn);
        logout = view.findViewById(R.id.logOutBtn);


        Email = (TextView) view.findViewById(R.id.EmailText);
        Username = (TextView) view.findViewById(R.id.UsernameText);
        FullName = (TextView) view.findViewById(R.id.NameText);
        ContactNo = (TextView) view.findViewById(R.id.NumberText);
        Brand = (TextView) view.findViewById(R.id.motorcycleTypeText);
        EmergencyContactNum = (TextView) view.findViewById(R.id.EmergencyContactText);

        String okayId = setMyArgument(email);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity here
                Intent intent = new Intent(getActivity(), EditDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id );
                bundle.putString("email", email );
                bundle.putString("fullName", fullName );
                bundle.putString("username", userName );
                bundle.putString("contact", contact );
                bundle.putString("brand", brand );
                bundle.putString("emergency", emergency );
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity here
                getActivity().finish();
            };
        });



        return view;
    }

    private void UserDetails(){
        LoginPage loginPage = new LoginPage();
    }

    class HttpRequestTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {

                // Create a URL object with the endpoint you want to make the request to
                URL url = new URL("http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=read&id="+id);

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
                try {
                    JSONObject user = new JSONObject(responseData);
                    id = user.getString("id");
                    String lastname = user.getString("sLastname");
                    String firstname = user.getString("sFirstname");
                    fullName = firstname + " " + lastname;
                    email = user.getString("sEmail");
                    userName = user.getString("sUsername");
                    contact = user.getString("sContact");
                    brand = user.getString("sModel");
                    emergency = user.getString("sEContact");
                    Email.setText(email);
                    FullName.setText(fullName);
                    ContactNo.setText(contact);
                    Username.setText(userName);
                    Brand.setText(brand);
                    EmergencyContactNum.setText(emergency);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Log.d("My User", responseData);
            } else {
                // Handle the case when the request fails
            }
        }
    }
}


