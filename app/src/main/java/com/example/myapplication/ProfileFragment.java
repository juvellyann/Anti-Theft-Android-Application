package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ProfileFragment newInstance} factory method to
// * create an instance of this fragment.
// */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    String id = null, email = null, fullName = null, userName = null, contact= null, brand = null, emergency = null;

    public ProfileFragment(String email, String fullName, String userName, String contact, String brand, String emergency) {
        // Required empty public constructor
        this.email = email;
        this.fullName = fullName;
        this.userName = userName;
        this.contact = contact;
        this.brand = brand;
        this.emergency = emergency;
    }

    public String setMyArgument(String Id) {
        this.email= Id;
        return Id;
    }

//    public static ProfileFragment newInstance(String Id){
//        ProfileFragment profileFragment = new ProfileFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("userId", Id);
//        profileFragment.setArguments(bundle);
//        return profileFragment;
//    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ProfileFragment.
//     */
    // TODO: Rename and change types and number of parameters
//    public static ProfileFragment newInstance(String param1, String param2) {
//        ProfileFragment fragment = new ProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }



    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        if(getArguments() != null){
            email = getArguments().getString("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
//        LoginPage loginPage = new LoginPage();
//        String link = this.getArguments().getString("link");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView Email, Username, FullName, ContactNo, Brand, EmergencyContactNum;


        Email = (TextView) view.findViewById(R.id.EmailText);
        Username = (TextView) view.findViewById(R.id.UsernameText);
        FullName = (TextView) view.findViewById(R.id.NameText);
        ContactNo = (TextView) view.findViewById(R.id.NumberText);
        Brand = (TextView) view.findViewById(R.id.motorcycleTypeText);
        EmergencyContactNum = (TextView) view.findViewById(R.id.EmergencyContactText);

        String okayId = setMyArgument(email);

//        Bundle bundle = this.getArguments();
//
//        email = bundle.getString("email");
//        fullName = bundle.getString("fullName");
//        username = bundle.getString("username");
//        contact = bundle.getString("contact");
//        brand = bundle.getString("brand");

//        Email.setText(email);
//        Username.setText(username);
//        FullName.setText(fullName);
//        ContactNo.setText(contact);
//        Brand.setText(brand);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
        //DatabaseReference reference;

        Email.setText(email);
        FullName.setText(fullName);
        ContactNo.setText(contact);
        Username.setText(userName);
        Brand.setText(brand);
        EmergencyContactNum.setText(emergency);
        //reference = FirebaseDatabase.getInstance().getReference().child("User");


        //Log.i("responseText", responseText);



//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String email = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Email").getValue(String.class);
//                String username = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Username").getValue(String.class);
//                String firstName = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FirstName").getValue(String.class);
//                String lastName = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LastName").getValue(String.class);
//                String contactNo= snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ContactNo").getValue(String.class);
//                String brand = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Brand").getValue(String.class);
//
//                Email.setText(email);
//                Username.setText(username);
//                FullName.setText(firstName + " " + lastName);
//                ContactNo.setText(contactNo);
//                Brand.setText(brand);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        return view;
    }

    private void UserDetails(){
        LoginPage loginPage = new LoginPage();
    }
}


