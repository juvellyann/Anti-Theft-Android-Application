package com.example.myapplication;

import android.util.Log;

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
}
