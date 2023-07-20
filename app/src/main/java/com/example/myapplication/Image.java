package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Image {
    String id;
    String dateStr;
    String time;
    public Image(String id, String img, String dateTime) {
        this.id = id;
        this.img = img;
        this.dateTime = dateTime;
        String[] token = dateTime.split(" ");
        dateStr = token[0];
        time = token[1];
    }

    String img;
    String dateTime;

    public String getDate(){

        LocalDate date = null;
        String parsedDate = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDate.parse(this.dateStr, DateTimeFormatter.ISO_DATE);
            String year = String.valueOf(date.getYear());
            String month = date.getMonth().name();
            String day = String.valueOf(date.getDayOfMonth());
            month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
            parsedDate = month+" "+day+", "+year;
        }
        Log.d("Parsed data",parsedDate);
        return parsedDate;
    }

    public String getTime(){
        LocalTime parsedTime = null;
        String formattedTime = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
            formattedTime = parsedTime.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
        }

        return formattedTime;
    }

    public void deleteImageFromDb(int did){
        ImageHTTP imageHTTP = new ImageHTTP();
        imageHTTP.execute(id+"",did+"");
    }
}

class ImageHTTP extends AsyncTask {

    @Override
    protected String doInBackground(Object[] objects) {
        try {

            // Create a URL object with the endpoint you want to make the request to
            String imgId = objects[0].toString();
            String did = objects[1].toString();
            URL url = new URL("http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/db.php?action=deleteImg&img="+imgId+"&did="+did);

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
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }
}
