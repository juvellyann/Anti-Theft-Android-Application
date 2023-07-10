package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditDetails extends AppCompatActivity {
    String id = null, email = null, fullName = null, userName = null, contact= null, brand = null, emergency = null, password = null;
    String firstName = null, lastName = null;
    EditText usernameEditText,emailEditText,firstNameEditText,lastNameEditText,contactEditText,brandEditText,emergencyEditText, passEditText,rePassEditText;

    Button editBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        setContentView(R.layout.edit_details);
        editBtn = findViewById(R.id.BtnEdit);
        if (extras != null) {
            id = extras.getString("id");
            email = extras.getString("email");
            fullName = extras.getString("fullName");
            userName = extras.getString("username");
            contact = extras.getString("contact");
            brand = extras.getString("brand");
            emergency = extras.getString("emergency");

            String[] token = fullName.split(" ");
            firstName = token[0];
            lastName = token[1];
            usernameEditText = findViewById(R.id.InputUsernameEdit);
            emailEditText = findViewById(R.id.InputEmailEdit);
            firstNameEditText = findViewById(R.id.InputFirstNameEdit);
            lastNameEditText = findViewById(R.id.InputLastNameEdit);
            contactEditText = findViewById(R.id.InputContactNoEdit);
            brandEditText = findViewById(R.id.InputBrandEdit);
            emergencyEditText = findViewById(R.id.inputEmergency);
            passEditText = findViewById(R.id.InputPassEdit);
            rePassEditText = findViewById(R.id.InputReEnterPassEdit);

            usernameEditText.setText(userName);
            emailEditText.setText(email);
            firstNameEditText.setText(firstName);
            lastNameEditText.setText(lastName);
            contactEditText.setText(contact);
            brandEditText.setText(brand);
            emergencyEditText.setText(emergency);


            passEditText.setText("mypassword");
            rePassEditText.setText("mypassword");

        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity here
                userName = usernameEditText.getText().toString().trim();
                email = emailEditText.getText().toString().trim();
                firstName = firstNameEditText.getText().toString().trim();
                lastName = lastNameEditText.getText().toString().trim();
                contact = contactEditText.getText().toString().trim();
                brand = brandEditText.getText().toString().trim();
                emergency = emergencyEditText.getText().toString().trim();
                String pass = passEditText.getText().toString().trim();
                String repass = rePassEditText.getText().toString().trim();

                // Perform validation
                if (TextUtils.isEmpty(userName)
                        || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(firstName)
                        || TextUtils.isEmpty(lastName)
                        || TextUtils.isEmpty(contact)
                        || TextUtils.isEmpty(brand)
                        || TextUtils.isEmpty(emergency)
                        || TextUtils.isEmpty(pass)
                        || TextUtils.isEmpty(repass)
                ) {
                    Toast.makeText(EditDetails.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!pass.equals(repass)){
                    Toast.makeText(EditDetails.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }

                password = pass;
                fullName = firstName+" "+lastName;
                new HttpRequestTask().execute();
            }
        });
        // Your code for initializing the activity
    }

    class HttpRequestTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {

                // Create a URL object with the endpoint you want to make the request to
                URL url = new URL("http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=update&id="+id+"&sUsername="+userName+"&sPassword="+password+"&sFirstname="+firstName+"&sLastname="+lastName+"&sContact="+contact+"&sEmail="+email+"&sModel="+brand+"&sEContact="+emergency+"");

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
                    Log.d("Edit Response",responseData);

                    String status = responseJson.getString("status");
                    String message = responseJson.getString("message");
                    if(status.equals("success")){
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("email", email );
                        bundle.putString("fullName", fullName );
                        bundle.putString("username", userName );
                        bundle.putString("contact", contact );
                        bundle.putString("brand", brand );
                        bundle.putString("emergency", emergency );
                        intent.putExtras(bundle);
                        setResult(1, intent);
                        finish();
                    }
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
