package com.example.a2hcomic.db;


import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;

public class FirebaseService {
    private FirebaseDatabase mDatabase;
    //private FirebaseStorage mStorage;

    //Khai bao cac đường dẫn
    private DatabaseReference mDBRef, mUserRef, mComicRef, mChapterRef;

    public FirebaseService() {
        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference("2HComic"); // đg dẫn tới DB

        // dg dẫn tới các table
        mUserRef = mDBRef.child("users");
        mComicRef = mDBRef.child("comics");
        mChapterRef = mDBRef.child("chapters");
    }

    // Các phương thức getter cho các đối tượng Firebase
    public DatabaseReference getUserRef() {
        return mUserRef;
    }
    public DatabaseReference getComicRef() {
        return mComicRef;
    }
    public DatabaseReference getChapterRef() {
        return mChapterRef;
    }
    }

