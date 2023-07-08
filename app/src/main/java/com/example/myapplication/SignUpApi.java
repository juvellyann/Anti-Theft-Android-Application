package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SignUpApi extends AsyncTask {
    String email, password, username, reEnterPassword, contactNo, brandModel, firstname, lastname;
    private Context context;

    public SignUpApi(String textEmail, String textPassword, String textUsername, String textContact, String textBrand, String textFirstname, String textLastname){
        this.context = context;
        this.email = textEmail;
        this.username = textUsername;
        this.password = textPassword;
        this.contactNo = textContact;
        this.brandModel = textBrand;
        this.firstname = textFirstname;
        this.lastname = textLastname;
    }

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
            String email = objects[5].toString();
            String brand = objects[6].toString();

            String link = "http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=create&sUsername="+username+"&sPassword="+password+"&sFirstname="+firstname+"&sLastname="+lastname+"&sContact="+contact+"&sEmail="+email+"&sModel="+brand;
            String data = "&s" + URLEncoder.encode("Username", "UTF-8") + "=" +
                    URLEncoder.encode(username, "UTF-8");
            data += "&s" + URLEncoder.encode("Password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");
            data += "&s" + URLEncoder.encode("Firstname", "UTF-8") + "=" +
                    URLEncoder.encode(firstname, "UTF-8");
            data += "&s" + URLEncoder.encode("Lastname", "UTF-8") + "=" +
                    URLEncoder.encode(lastname, "UTF-8");
            data += "&s" + URLEncoder.encode("Contact", "UTF-8") + "=" +
                    URLEncoder.encode(contact, "UTF-8");
            data += "&s" + URLEncoder.encode("Email", "UTF-8") + "=" +
                    URLEncoder.encode(email, "UTF-8");
            data += "&s" + URLEncoder.encode("Model", "UTF-8") + "=" +
                    URLEncoder.encode(brand, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return sb.toString();
        } catch (Exception e){
            return new String(e.getMessage());
        }
    }


    protected void onPostExecute(){

    }

}
