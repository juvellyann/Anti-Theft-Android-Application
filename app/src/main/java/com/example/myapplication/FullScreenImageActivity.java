package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        // Retrieve the image URL from the intent extras
        String imageUrl = getIntent().getStringExtra("imageUrl");

        imageView = findViewById(R.id.fullScreenImageView);
        Log.d("Image in full",imageUrl);
        // Load the image into the ImageView using Picasso or any other image loading library
        Picasso.get().load("http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/uploads/"+imageUrl).into(imageView);
    }
}
