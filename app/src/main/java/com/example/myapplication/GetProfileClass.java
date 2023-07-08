package com.example.myapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class GetProfileClass extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] objects) {

        try{
            String baseurl = "http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=read&id=1";
            URL url = new URL(baseurl);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

//            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

//                Intent intent = new Intent(LoginPage.this, HomePage.class);
//                startActivity(intent);
//                finish();

            return sb.toString();
        }catch(Exception e){

        }

        return null;
    }
}
