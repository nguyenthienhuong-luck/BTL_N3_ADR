package com.example.a2hcomic.db;



import com.example.a2hcomic.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;

public class FirebaseService {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef, mUserRef;

    public FirebaseService() {
        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference("2HComic"); // đg dẫn tới DB
        mUserRef = mDBRef.child("users");
    }

    // Các phương thức getter cho các đối tượng Firebase
    public DatabaseReference getDBRef() {
        return mDBRef;
    }

    public DatabaseReference getUserRef() {
        return mUserRef;
    }

    public void createUser(User user) {
        String id = mUserRef.push().getKey();
        user.setId(id);
        mUserRef.child(user.getId()).setValue(user);
    }

}