package com.example.a2hcomic.db;



import com.example.a2hcomic.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;

public class FirebaseService {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef, mUserRef, mComicRef;

    public FirebaseService() {
        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference("2HComic"); // đg dẫn tới DB
        mUserRef = mDBRef.child("users");
        mComicRef = mDBRef.child("comics");
    }

    // Các phương thức getter cho các đối tượng Firebase
    public DatabaseReference getDBRef() {
        return mDBRef;
    }

    public DatabaseReference getUserRef() {
        return mUserRef;
    }

    public DatabaseReference getComicRef() {
        return mComicRef;
    }

    public void createUser(User user) {
        String id = mUserRef.push().getKey();
        user.setId(id);
        mUserRef.child(user.getId()).setValue(user);
    }

}