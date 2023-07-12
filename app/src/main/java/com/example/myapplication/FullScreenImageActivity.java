package com.example.myapplication;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_full_screen_image);

        // Retrieve the image URL from the intent extras
        String imageUrl = getIntent().getStringExtra("imageUrl");

        imageView = findViewById(R.id.fullScreenImageView);
        Log.d("Image in full",imageUrl);
        // Load the image into the ImageView using Picasso or any other image loading library
        Picasso.get().load("http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/uploads/"+imageUrl).into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close the activity when the back button is clicked
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
