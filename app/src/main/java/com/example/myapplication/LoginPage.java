package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.textclassifier.TextClassification;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;

public class LoginPage extends AppCompatActivity {
    EditText username, password, token;
    TextView edit;
    Button signUp, login;
    String loginUrl;
    boolean isLogged = false;
    private FirebaseAuth mAuth;
    // Initialize Firebase Auth
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private ProgressDialog processDialog;
    private JSONArray resultJsonArray;
    String value = "";
    String linkUrl = "";
    String id=null, email = null, fullName = null, userName = null, contact= null, brand = null, emergency = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstance){
        super.onCreate(savedInstance);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.login_page);

        SharedPreferences sharedPref = this.getSharedPreferences("override",Context.MODE_PRIVATE);
        int iParkingOverride = sharedPref.getInt("iParking", -1);
        int iEngineOverride = sharedPref.getInt("iEngine", -1);
        Log.d("Override iParking", iParkingOverride+"");
        Log.d("Override iEngine", iEngineOverride+"");

        if(iParkingOverride != -1 && iEngineOverride != -1){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.loading_dialog_view);
            builder.setTitle("Syncing");
            AlertDialog dialog = builder.create();
            dialog.show();
        }


        username = findViewById(R.id.InputUsername);
        password = findViewById(R.id.InputPassword);
        signUp = findViewById(R.id.SignUpBtn);
        login = findViewById(R.id.LoginBtn);

        username.setText("johndoe");
        password.setText("mypassword");


        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate())
        {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(), "Device does not have fingerprint",Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(), "Not working",Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(), "No fingerprint Assigned",Toast.LENGTH_SHORT).show();

        }

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginPage.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                new LoginUser().execute("johndoe","mypassword");
//                Intent intent = new Intent(LoginPage.this, HomePage.class);
//                startActivity(intent);
//                finish();
//                Toast.makeText(getApplicationContext(),
//                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        biometricPrompt.authenticate(promptInfo);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, SignUpPage.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to remove
                ConnectionHelper connectionHelper = new ConnectionHelper(getApplicationContext());
//                Log.d("Connection Helper", connectionHelper+"");
//                if(!connectionHelper.haveNetworkConnection()){
//                    Intent intent = new Intent(LoginPage.this, HomePage.class);
//                    startActivity(intent);
//                    return;
//                }

                //String userId = username.getText().toString().trim();
                //String Password = password.getText().toString().trim();

                String email, Password;
                email = String.valueOf(username.getText());
                Password = String.valueOf(password.getText());

//                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                String ssidName = wifiManager.getConnectionInfo().getSSID();
//                ssidName = ssidName.replace("\"","");
//                Log.d("SSID NAME", ssidName);
//
//                if(ssidName.equals("Anti-thief")){
//                    Intent intent = new Intent(LoginPage.this, HomePage.class);
//                    startActivity(intent);
//                    return;
//                }

                boolean isLocal = pingNetwork("192.168.4.1");
                if(isLocal || !connectionHelper.haveNetworkConnection()){
                    Intent intent = new Intent(LoginPage.this, HomePage.class);
                    intent.putExtra("isLocal", isLocal);
                    startActivity(intent);
                    return;
                }

                if(email.isEmpty()){
                    username.setError("Email is required");
                    username.requestFocus();
                    return;
                }


                if(Password.isEmpty()){
                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                }

                if(Password.length()<6){
                    password.setError("Minimum password length is 6 characters");
                    password.requestFocus();
                    return;
                }
                //loginUser(email, Password);
                LoginUser loginUser = new LoginUser();
                loginUser.execute(email,Password);
            }
        });


//        FirebaseMessaging.getInstance().getToken()
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
//                        System.out.println(tokenId);
//                        Toast.makeText(LoginPage.this, "Your device registration token is " + tokenId, Toast.LENGTH_SHORT).show();
//
//                        token.setText(tokenId);
//                    }
//                });
    }

    public class LoginUser extends AsyncTask {
//        URL createUserUrl = new URL("http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=create&sUsername="+username + "&sPassword="+password+"&sFirstname="+firstName+"&sLastname="+lastName+"&sContact="+contactNo+"&sEmail="+email+"&sModel="+brand);
//        HttpURLConnection urlConnection = (HttpsURLConnection) createUserUrl.openConnection();
//        }
//        Toast.makeText(SignUpPage.this, "Sign up successful", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(SignUpPage.this,LoginPage.class);
//        startActivity(intent);

        //String username, password;
        User currentUser;
        protected void onPreExecute(){
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                String username = objects[0].toString();
                String password = objects[1].toString();

                linkUrl = "http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=login&sUsername="+username+"&sPassword="+password;
//                linkUrl = "http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=login&sUsername=johndoe&sPassword=mypassword";



                String data = "&s" + URLEncoder.encode("sUsername", "UTF-8") + "=" +
                        URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("sPassword", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8");

                String tempData = URLEncoder.encode("sEmail", "UTF-8");
                URL url = new URL(linkUrl);
                URLConnection conn = url.openConnection();


                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                HttpResponse response = null;
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();
                    request.setURI(new URI(
                            linkUrl));
                    response = client.execute(request);
                    int status = response.getStatusLine().getStatusCode();
                    Log.d("Status Code",status+"");

//                    if(status == 200){
                        HttpEntity entity = response.getEntity();
                        String mydata = EntityUtils.toString(entity);
                        Log.d("Temp",mydata);
                        JSONObject json = new JSONObject(mydata);
                        JSONObject user = json.getJSONObject("user");

                        if(json.getString("status") == "error"){
                            Log.d("Logged Message","Login Failed");
                        }else{
                            isLogged = true;
                            Log.d("Logged Message","Login Success");
                            Log.d("Current User",user.getString("sFirstname"));

                            id = user.getString("id");
                            String lastname = user.getString("sLastname");
                            String firstname = user.getString("sFirstname");
                            fullName = firstname + " " + lastname;
                            email = user.getString("sEmail");
                            userName = user.getString("sUsername");
                            contact = user.getString("sContact");
                            brand = user.getString("sModel");
                            emergency = user.getString("sEContact");
                            Log.d("Current User",firstname);
                            currentUser = new User(id,email,firstname,lastname,username,contact,brand, emergency);
                            Log.d("Current User Object",currentUser.toString());
                        }

//                    }


                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String responseText = null;

                //Log.i("responseText", responseText);

//                GetUserInfo(linkUrl);
//                Intent intent = new Intent(LoginPage.this, HomePage.class);
//                startActivity(intent);
//                finish();

                return sb.toString();
            } catch (Exception e){
                return new String(e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(isLogged){
                Intent intent = new Intent(LoginPage.this, HomePage.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id );
                bundle.putString("email", email );
                bundle.putString("fullName", fullName );
                bundle.putString("username", userName );
                bundle.putString("contact", contact );
                bundle.putString("brand", brand );
                bundle.putString("emergency", emergency );

                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
            Log.d("User", o.toString());

//            finish();
        }
    }

    public void GetUserInfo(String link){

        HttpResponse response = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(
                    link));
            response = client.execute(request);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String responseText = null;
        try {
            // Convert String to json object
            responseText = EntityUtils.toString(response.getEntity());
            JSONObject json = new JSONObject(responseText);

            // get LL json object
            JSONObject user = json.getJSONObject("user");

            // get value from LL Json Object
            value=user.getString("id"); //<< get value here
            String firstname = user.getString("sFirstname");
            Log.d("Current User",firstname);
//            String lastname = user.getString("sLastname");
//            fullName = firstname + " " + lastname;
//
//            email = user.getString("sEmail");
//            userName = user.getString("sUsername");
//            contact = user.getString("sContact");
//            brand = user.getString("sModel");

//            ProfileFragment fragInfo = new ProfileFragment();
//            fragInfo.setArguments(bundle);
//            getSupportFragmentManager().beginTransaction().replace(R.id.profile, fragInfo).commit();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("Parse Exception", e + "");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Log.i("responseText", responseText);
    }

    public String getValue() {
        return value;
    }

    public boolean pingNetwork(String ipAddress){
        boolean reachable = false;
       try {
           Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 -w 1 "+ipAddress);
           reachable = (p1.waitFor() == 0);
       }catch (Exception e){
           e.printStackTrace();
           Log.d("Ping","e");
           reachable = false;
       }
        Log.d("Ping",reachable+"");
        return reachable;
    }

    public class SetParking extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                String parkingMode = objects[0].toString();
                    linkUrl = "http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=setParking&status=" + parkingMode + "&did=1";
                URL url = new URL(linkUrl);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(linkUrl));
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
    }

    public class SetEngine extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                String engine = objects[0].toString();
                    linkUrl = "http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=setEngine&status=" + engine + "&did=1";

                URL url = new URL(linkUrl);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(linkUrl));
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

    }

}
