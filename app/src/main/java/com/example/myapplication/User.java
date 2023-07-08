package com.example.myapplication;

public class User {
    public String Email, FirstName, LastName, Username, ContactNo, Brand, Emergency;
    String id;
    public User(String id,String email, String firstName, String lastName, String username, String contactNo, String brand, String emergency){
        this.id = id;
        this.Email = email;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Username = username;
        this.ContactNo = contactNo;
        this.Brand = brand;
        this.Emergency = emergency;
    }
}
