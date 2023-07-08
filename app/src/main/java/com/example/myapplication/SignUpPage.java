package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;


public class SignUpPage extends AppCompatActivity {
    EditText email, password, username, reEnterPassword, contactNo, brandModel, firstname, lastname, emergencyContact;
    Button signUpButton;
    FirebaseAuth mAuth;
    String textToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.InputEmailSignUp);
        firstname = findViewById(R.id.InputFirstNameSignUp);
        lastname = findViewById(R.id.InputLastNameSignUp);
        username = findViewById(R.id.InputUsernameSignUp);
        contactNo = findViewById(R.id.InputContactNoSignUp);
        reEnterPassword = findViewById(R.id.InputReEnterPassSignUp);
        brandModel = findViewById(R.id.InputBrandSignUp);
        password = findViewById(R.id.InputPassSignUp);
        signUpButton = findViewById(R.id.BtnSignUp);
        emergencyContact = findViewById(R.id.inputEmergency);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail, textPassword, textUsername, textFirstName, textLastName, textReEnterPassword, textContactNo, textBrandModel, textEmergencyContact;
                textEmail = String.valueOf(email.getText());
                textPassword = String.valueOf(password.getText());
                textFirstName = String.valueOf(firstname.getText());
                textLastName = String.valueOf(lastname.getText());
                textUsername = String.valueOf(username.getText());
                textReEnterPassword = String.valueOf(reEnterPassword.getText());
                textContactNo = String.valueOf(contactNo.getText());
                textBrandModel = String.valueOf(brandModel.getText());
                textEmergencyContact = String.valueOf(emergencyContact.getText());

//                FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            System.out.println("Fetching FCM registration token failed");
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String tokenId = task.getResult();
//
//                        // Log and toast
//                        //System.out.println(te);
//                        //Toast.makeText(SignUpPage.this, "Your device registration token is " + tokenId, Toast.LENGTH_SHORT).show();
//                        textToken = String.valueOf(tokenId);
//                    }
//                });


                if(TextUtils.isEmpty(textEmail)){
                    username.setError("Email is required");
                    username.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(textPassword)){
                    username.setError("Password is required");
                    username.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(textUsername)){
                    username.setError("Username is required");
                    username.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(textFirstName)){
                    firstname.setError("First Name is required");
                    firstname.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(textLastName)){
                    lastname.setError("Last Name is required");
                    lastname.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(textReEnterPassword)){
                    username.setError("Please re enter password");
                    username.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(textContactNo)){
                    username.setError("Enter your contact no.");
                    username.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(textBrandModel)){
                    username.setError("Brand and Model is required");
                    username.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(textEmergencyContact)){
                    username.setError("Emergency Contact No. is required");
                    username.requestFocus();
                    return;
                }

                if(!textPassword.equals(textReEnterPassword)){
                    Toast.makeText(SignUpPage.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(textEmail.isEmpty() || textPassword.isEmpty() || textUsername.isEmpty() || textReEnterPassword.isEmpty() || textContactNo.isEmpty() || textBrandModel.isEmpty()){
                    Toast.makeText(SignUpPage.this, "Enter the necessary information", Toast.LENGTH_SHORT).show();
                    return;
                }
                //SignUp(textEmail,textPassword,textUsername,textFirstName,textLastName,textContactNo,textBrandModel);
                //CreateUser(textEmail,textPassword,textUsername,textFirstName,textLastName,textContactNo,textBrandModel);

                CreateUser createUser = new CreateUser();
                createUser.execute(textUsername,textPassword,textFirstName,textLastName,textContactNo,textEmergencyContact,textEmail,textBrandModel);
            }
        });
    }

    //Firebase
//    public void SignUp(String email, String password, String username, String firstName, String lastName, String contactNo,String brand){
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            User user = new User(email,firstName,lastName,username,contactNo,brand);
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("User")
//                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                    .setValue(user).addOnCompleteListener(task1 -> {
//                                        if(task1.isSuccessful()){
//                                            FirebaseNotification firebaseNotification = new FirebaseNotification();
//                                            firebaseNotification.onNewToken(textToken);
//                                            Toast.makeText(SignUpPage.this, "Sign up successful", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(SignUpPage.this,LoginPage.class);
//                                            startActivity(intent);
//                                        }
//                            });
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(SignUpPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                        }
//                });
//    }


    public class CreateUser extends AsyncTask {
        String email, password, username, reEnterPassword, contactNo, brandModel, firstname, lastname;
        private Context context;

        protected void onPreExecute(){
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                String username = objects[0].toString();
                String password = objects[1].toString();
                String firstname = objects[2].toString();
                String lastname = objects[3].toString();
                String contact = objects[4].toString();
                String emergency = objects[5].toString();
                String email = objects[6].toString();
                String brand = objects[7].toString();

                String link = "http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=create&sUsername="+username+"&sPassword="+password+"&sFirstname="+firstname+"&sLastname="+lastname+"&sContact="+contact+ "&sEContact="+emergency +"&sEmail="+email+"&sModel="+brand;
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            } catch (Exception e){
                return new String(e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            Toast.makeText(SignUpPage.this, "Sign up successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpPage.this,LoginPage.class);
            startActivity(intent);
        }
    }
}
