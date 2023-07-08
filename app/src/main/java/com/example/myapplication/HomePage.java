package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityMainBinding;

public class HomePage extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.location) {
                replaceFragment(new LocationFragment());
            } else if (itemId == R.id.gallery) {
                replaceFragment(new GalleryFragment());
            } else if (itemId == R.id.notifications) {
                replaceFragment(new NotificationsFragment());
            } else if (itemId == R.id.profile) {
                Bundle extras = getIntent().getExtras();
                String id = null, email = null, fullName = null, userName = null, contact= null, brand = null, emergency = null;
                if (extras != null) {
                    email = extras.getString("email");
                    fullName = extras.getString("fullName");
                    userName = extras.getString("username");
                    contact = extras.getString("contact");
                    brand = extras.getString("brand");
                    emergency = extras.getString("emergency");

                    Log.d("Current User Email",userName);
                }
                replaceFragment(new ProfileFragment(email, fullName, userName, contact, brand, emergency));
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }


}
