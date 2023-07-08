package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;



import java.net.HttpRetryException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AsyncTask<String, String, String> {

    EditText email, password;
    private Context context;
    private int byGetOrPost = 0;

    public LoginActivity(Context context, EditText email, EditText password, int flag){
        this.email = email;
        this.password = password;
        this.context = context;
        byGetOrPost = flag;
    }

    @Override
    protected String doInBackground(String... arg0) {
        if(byGetOrPost == 0){
            try {
                String email = (String)arg0[0];
                String password = (String)arg0[1];
                String loginUrl = "http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=login&sUsername="+ email +"&sPassword=" + password;

                URL url = new URL(loginUrl);

            } catch (Exception e){
                return new String("Exception" + e.getMessage());
            }
        }
        return null;
    }
}
